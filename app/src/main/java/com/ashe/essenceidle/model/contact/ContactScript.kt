package com.ashe.essenceidle.model.contact

import androidx.compose.runtime.Immutable
import com.ashe.essenceidle.model.database.CharacterState

@Immutable
abstract class ContactScript(val previousSteps: List<ScriptStep>, val currentStep: ScriptStep, var unread:Boolean) {
    abstract val contactFirstName: String
    abstract val contactLastName: String
    abstract val id: String

    /**
     * Should this contact be visible to the player
     * @return true if visible, false if not
     */
    abstract fun showContact(): Boolean

    /**
     * Create a copy of this ContractScript but stepped forward by given step.
     * @param step The next step
     * @param unread true if this contact script should be marked as unread
     * @return the new copied script
     */
    fun newStep(step: ScriptStep, unread: Boolean): ContactScript {
        return newCopy(previousSteps = previousSteps + currentStep, step = step, unread = unread)
    }

    /*
    * TODO: Check if there's a better way to hoist this duplicated functionality here instead of
    *  relying on the concrete classes to basically do the exact same thing, just substituting their
    *  class constructor
     */
    /**
     * Create a copy of this ContractScript, optionally with modified values
     * @param step The next step
     * @param previousSteps previous contact script steps for message history
     * @param unread true if this contact script should be marked as unread
     * @return the new copied script
     */
    abstract fun newCopy(step: ScriptStep? = null, previousSteps: List<ScriptStep>? = null, unread: Boolean?): ContactScript

    /**
     * Implement ScriptStep as an enum class within the concrete class to create the script steps
     * for that specific class.
     */
    interface ScriptStep{
        /**
         * Return all the messages that are relevant to this step of the script
         * @return A list of chat messages that will be displayed when this step is displayed
         */
        fun getMessages(): List<ChatMessage>

        /**
         * Check if this this script step is prepared to progress to another stage
         * @param characterState the current character state to check against
         * @return true if ready to progress, false if anything else
         */
        fun readyForProgression(characterState: CharacterState): Boolean

        /**
         * Get a list of chat messages to display to the player which lead to different steps
         * @return A list of ChatMessage paired with the ScriptStep they lead to
         */
        fun chatOptions(): List<Pair<ChatMessage, ScriptStep>>

        /**
         * Returns the next logical ScriptStep for steps that don't have chatOptions. This is used
         * for autonomous script updates that aren't relying on user input. You should implement
         * /either/ chatOptions or nextStep, but not both.
         * @return The next logical ScriptStep or null
         */
        fun nextStep(): ScriptStep?
    }

    fun fullName(): String {
        return "$contactFirstName $contactLastName"
    }
}

/**
 * @property text The text of the message
 * @property received if true, this message is treated as a received message, otherwise as if the user sent it
 */
class ChatMessage(val text: String, val received: Boolean) {

}