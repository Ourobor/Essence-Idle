package com.ashe.essenceidle.model.task

import android.util.Log
import com.ashe.essenceidle.model.CharacterState

private const val totalAttributePenalty = 500.0
private const val thisAttributePenalty = 2000.0
//TODO: This is probably should be in another file probably
fun calculateAttributeGain(totalAttributes: Int, thisAttribute: Int, amount: Int): Int {
    //Factors in order of importance
    //1: penalty the more total attribute points. As you get more attributes, it takes longer to
    //   get more
    val totalPenalty = 1.0 - (totalAttributes / (totalAttributes + totalAttributePenalty))
//    val totalPenalty = exp(0.0 - (totalAttributes * totalAttributes))

    //3: penalty for pushing an attribute above x(~20)% of the total pool max by ~40%? Encourage
    //   the player to keep their attributes at a minimum value relative to their overall attributes
    val minMaxPenalty = if((thisAttribute / totalAttributes) > 0.3){
        0.75
    }
    else{
        1.0
    }

    return (totalPenalty  * minMaxPenalty * amount).toInt()
}

private const val agilityTrainingTicks = 10
class AgilityTrainingAction: EssenceAction("Training Agility...", "AGI Train") {
    init {
        ticksRemaining = agilityTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        Log.d("EssenceIdle", "Agility:${characterState.agility}")
        val totalAttributes = (characterState.agility + characterState.power + characterState.endurance + characterState.spirit)
        characterState.agility +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.agility,
                amount = 10)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / agilityTrainingTicks)
    }
}

private const val powerTrainingTicks = 10
class PowerTrainingAction: EssenceAction("Training power...", "PWR Train") {
    init {
        ticksRemaining = powerTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        Log.d("EssenceIdle", "Agility:${characterState.agility}")
        val totalAttributes = (characterState.agility + characterState.power + characterState.endurance + characterState.spirit)
        characterState.power +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.power,
                amount = 10)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / powerTrainingTicks)
    }
}

private const val spiritTrainingTicks = 10
class SpiritTrainingAction: EssenceAction("Training Spirit...", "SPR Training") {
    init {
        ticksRemaining = spiritTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        Log.d("EssenceIdle", "Agility:${characterState.agility}")
        val totalAttributes = (characterState.agility + characterState.power + characterState.endurance + characterState.spirit)
        characterState.spirit +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.spirit,
                amount = 10)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / spiritTrainingTicks)
    }
}

private const val enduranceTrainingTicks = 10
class EnduranceTrainingAction: EssenceAction("Training Endurance...", "END Train") {
    init {
        ticksRemaining = enduranceTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        val totalAttributes = (characterState.agility + characterState.power + characterState.endurance + characterState.spirit)
        characterState.endurance +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.endurance,
                amount = 10)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / enduranceTrainingTicks)
    }
}
