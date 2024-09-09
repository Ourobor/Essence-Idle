package com.ashe.essenceidle.model.contact

import androidx.compose.runtime.Immutable
import com.ashe.essenceidle.model.database.CharacterState

class WatchfulRaven(previousSteps: List<ScriptStep>,
                    currentStep: ScriptStep = Steps.UNINTRODUCED,
                    unread: Boolean = false
) : ContactScript(previousSteps, currentStep, unread) {
    override val id: String = "WR"
    override val contactFirstName: String = "Watchful"
    override val contactLastName: String = "Raven"
    override fun showContact(): Boolean {
        return currentStep != Steps.UNINTRODUCED
    }

    override fun newCopy(
        step: ScriptStep?,
        previousSteps: List<ScriptStep>?,
        unread: Boolean?
    ): ContactScript {
        return WatchfulRaven(previousSteps = previousSteps ?: this.previousSteps, step ?: this.currentStep, unread ?: this.unread)
    }


//    override fun newStep(step: ScriptStep, unread: Boolean): WatchfulRaven {
//        return WatchfulRaven(previousSteps = previousSteps + currentStep, step, unread)
//    }

    @Immutable
    enum class Steps: ScriptStep {
        UNINTRODUCED {
            override fun getMessages(): List<ChatMessage> {
                return listOf()
            }

            override fun readyForProgression(characterState: CharacterState): Boolean {
                return characterState.totalEssenceCollected >= 100
            }

            override fun chatOptions(): List<Pair<ChatMessage, ScriptStep>> {
                return listOf()
            }

            override fun nextStep(): ScriptStep {
                return INTRODUCED
            }
        },
        INTRODUCED{
            override fun getMessages(): List<ChatMessage> {
                return listOf(
                    ChatMessage(text = "HELLO", received = true),
                    ChatMessage(text = "I AM WATCHFUL RAVEN. WHO ARE YOU?", received = true),
                )
            }

            override fun readyForProgression(characterState: CharacterState): Boolean {
                return true
            }

            override fun chatOptions(): List<Pair<ChatMessage, ScriptStep>> {
                return listOf(
                    Pair(ChatMessage("Nobody.", false), STAGETWO),
                )
            }

            override fun nextStep(): ScriptStep? {
                return null
            }
        },
        STAGETWO{
            override fun getMessages(): List<ChatMessage> {
                return listOf(
                    ChatMessage(text = "Nobody.", received = false),
                    ChatMessage(text = "NO ONE IS NOBODY, NOBODY.", received = true),
                    ChatMessage(text = "WHAT WILL YOU DO WITH POWER I WONDER? CHECK YOUR APP FOR THE SOUL FORGE.", received = true),
                    ChatMessage(text = "I'LL BE IN TOUCH.", received = true)
                )
            }

            override fun readyForProgression(characterState: CharacterState): Boolean {
                return (characterState.enduranceUnlocked && characterState.powerUnlocked &&
                        characterState.speedUnlocked && characterState.spiritUnlocked)
            }

            override fun chatOptions(): List<Pair<ChatMessage, ScriptStep>> {
                return listOf()
            }

            override fun nextStep(): ScriptStep {
                return RITUALINTRODUCTION
            }
        },
        RITUALINTRODUCTION{
            override fun getMessages(): List<ChatMessage> {
                return listOf(
                    ChatMessage(text = "NOBODY. YOU'VE BEEN BUSY. I'M PLEASED.", received = true),
                    ChatMessage(text = "CHECK YOUR APP. I UNLOCKED REMOTE RITUAL RIGHTS FOR YOU", received = true)
                )
            }

            override fun readyForProgression(characterState: CharacterState): Boolean {
                return false
            }

            override fun chatOptions(): List<Pair<ChatMessage, ScriptStep>> {
                return listOf()
            }

            override fun nextStep(): ScriptStep? {
                return null
            }

        }
    }
}