package com.ashe.essenceidle.model.task

import com.ashe.essenceidle.model.data.CharacterState

private const val totalAttributePenalty = 500.0
private const val thisAttributePenalty = 2000.0
private const val attributeTrainingTicks = 6

/**
 * Calculate the amount of Attribute points a player should gain given the maximum value and the
 * current state. Applies penalties to overall gain based on current game state and returns the
 * that amount
 * @param totalAttributes the sum of all attribute points
 * @param thisAttribute the value of the attribute gaining points
 * @param amount the maximum amount of attribute points to be added
 *
 * @return modified attribute point gain
 */
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

class SpeedTrainingAction: EssenceAction("Training Speed...", "SPD Train") {
    init {
        ticksRemaining = attributeTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
//        Log.d("EssenceIdle", "Speed:${characterState.speed}")
        val totalAttributes = (characterState.speed + characterState.power + characterState.endurance + characterState.spirit)
        characterState.speed +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.speed,
                amount = 10)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / attributeTrainingTicks)
    }
}

class PowerTrainingAction: EssenceAction("Training power...", "PWR Train") {
    init {
        ticksRemaining = attributeTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        val totalAttributes = (characterState.speed + characterState.power + characterState.endurance + characterState.spirit)
        characterState.power +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.power,
                amount = 10)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / attributeTrainingTicks)
    }
}

class SpiritTrainingAction: EssenceAction("Training Spirit...", "SPR Training") {
    init {
        ticksRemaining = attributeTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        val totalAttributes = (characterState.speed + characterState.power + characterState.endurance + characterState.spirit)
        characterState.spirit +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.spirit,
                amount = 10)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / attributeTrainingTicks)
    }
}

class EnduranceTrainingAction: EssenceAction("Training Endurance...", "END Train") {
    init {
        ticksRemaining = attributeTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        val totalAttributes = (characterState.speed + characterState.power + characterState.endurance + characterState.spirit)
        characterState.endurance +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.endurance,
                amount = 10)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / attributeTrainingTicks)
    }
}
