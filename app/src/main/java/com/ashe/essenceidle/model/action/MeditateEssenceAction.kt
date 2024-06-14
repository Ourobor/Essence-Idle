package com.ashe.essenceidle.model.action

import com.ashe.essenceidle.model.database.CharacterState

private const val meditationTicks = 6
private const val meditateEssenceGain = 10

class MeditateEssenceAction() : EssenceAction("Meditating...", "Meditate") {
    init {
        ticksRemaining = meditationTicks
    }

    override fun executeAction(characterState: CharacterState){
        characterState.essenceStrength += meditateEssenceGain
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / meditationTicks)
    }
}