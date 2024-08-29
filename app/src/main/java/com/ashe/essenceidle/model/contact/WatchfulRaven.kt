package com.ashe.essenceidle.model.contact

import androidx.compose.runtime.Immutable
import com.ashe.essenceidle.model.database.CharacterState

class WatchfulRaven(previousSteps: List<ScriptStep>) : ContactScript(previousSteps) {
    override val id: String = "WR"
    override val contactFirstName: String = "Watchful"
    override val contactLastName: String = "Raven"
    override var currentStep: ScriptStep = Steps.INTRODUCED
    override fun showContact(): Boolean {
        return currentStep != Steps.UNINTRODUCED
    }

    override fun nextStep(step: ScriptStep): WatchfulRaven {
        val newContact = WatchfulRaven(previousSteps = previousSteps + currentStep)
        newContact.currentStep = step
        return newContact
    }

    @Immutable
    enum class Steps: ScriptStep {
        UNINTRODUCED {
            override fun getMessages(): List<ChatMessage> {
                return listOf()
            }

            override fun readyForProgression(characterState: CharacterState): Boolean {
                return false
            }

            override fun chatOptions(): List<Pair<ChatMessage, ScriptStep>> {
                return listOf()
            }
        },
        INTRODUCED{
            override fun getMessages(): List<ChatMessage> {
                return listOf(
                    ChatMessage(text = "HELLO I AM A BIRD CAW CAW", received = true),
                    ChatMessage(text = "I TOO AM A BIRD CAW CAW", received = false),
                    ChatMessage(
                        text = "COOL WE SHOULD TEAM UP FOR MISCHIEF AND SHINY THINGS. ALSO WHAT " +
                                "KIND OF BIRD ARE YOU? HONESTLY I'M JUST SAYING A LOT OF SHIT TO SEE IF " +
                                "WORDWRAP WORKS", received = true
                    )
                )
            }

            override fun readyForProgression(characterState: CharacterState): Boolean {
                return characterState.essenceStrength >= 300
            }

            override fun chatOptions(): List<Pair<ChatMessage, ScriptStep>> {
                return listOf(
                    Pair(ChatMessage("I AM A CROW!", false), STAGETWO )
                )
            }
        },
        STAGETWO{
            override fun getMessages(): List<ChatMessage> {
                return listOf(
                    ChatMessage(text = "I AM A CROW!", received = false),
                    ChatMessage(text = "FUCKING COOL", received = true)
                )
            }

            override fun readyForProgression(characterState: CharacterState): Boolean {
                return false
            }

            override fun chatOptions(): List<Pair<ChatMessage, ScriptStep>> {
                return listOf()
            }
        };
    }
}