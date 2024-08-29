package com.ashe.essenceidle.model.contact

import com.ashe.essenceidle.model.database.CharacterState

//TODO: change constructors to make sense. nextStep would benefit from an empty constructor
abstract class ContactScript(val previousSteps: List<ScriptStep>) {
    abstract val contactFirstName: String
    abstract val contactLastName: String
    abstract val id: String
    //This is left abstract so each each concrete ContactScript can define its own default step
    abstract var currentStep: ScriptStep

    /**
     * Should this contact be visible to the player
     * @return true if visible, false if not
     */
    abstract fun showContact(): Boolean

    abstract fun nextStep(step: ScriptStep): ContactScript

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