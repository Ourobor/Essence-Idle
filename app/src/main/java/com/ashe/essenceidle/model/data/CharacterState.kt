package com.ashe.essenceidle.model.data

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

/**
 * Contains the state of a character save. Please note this class **only** contains values that
 * should be saved to the database. Anything else should be stored elsewhere, like a view controller
 */
@Suppress("PropertyName")
class CharacterState : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var essenceStrength: Int = 0
    var speed: Int = 100
    var power: Int = 100
    var spirit: Int = 100
    var endurance: Int = 100
    var soulForgeUnlocked: Boolean = false
    var multipleActionsUnlocked: Boolean = false
    var speedUnlocked: Boolean = false
    var powerUnlocked: Boolean = false
    var spiritUnlocked: Boolean = false
    var enduranceUnlocked: Boolean = false

    //TODO Check in on this to make sure there isn't a better way
    fun clone(): CharacterState {
        val newCharacterState = CharacterState()

        newCharacterState._id = this._id
        newCharacterState.essenceStrength = this.essenceStrength
        newCharacterState.soulForgeUnlocked = this.soulForgeUnlocked
        newCharacterState.multipleActionsUnlocked = this.multipleActionsUnlocked
        newCharacterState.speedUnlocked = this.speedUnlocked
        newCharacterState.powerUnlocked = this.powerUnlocked
        newCharacterState.spiritUnlocked = this.spiritUnlocked
        newCharacterState.enduranceUnlocked = this.enduranceUnlocked
        newCharacterState.speed = this.speed
        newCharacterState.power = this.power
        newCharacterState.spirit = this.spirit
        newCharacterState.endurance = this.endurance

        return newCharacterState
    }
}