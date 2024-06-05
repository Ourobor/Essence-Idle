package com.ashe.essenceidle.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

/**
 * Contains the state of a character save. Please note this class **only** contains values that
 * should be saved to the database. Anything else should be stored elsewhere, like a view controller
 */
class CharacterState : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var essenceStrength: Int = 0
    var agility: Int = 0
    var power: Int = 0
    var spirit: Int = 0
    var endurance: Int = 0
    var soulForgeUnlocked: Boolean = false
    var multipleActionsUnlocked: Boolean = false
    var agilityUnlocked: Boolean = false
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
        newCharacterState.agilityUnlocked = this.agilityUnlocked
        newCharacterState.powerUnlocked = this.powerUnlocked
        newCharacterState.spiritUnlocked = this.spiritUnlocked
        newCharacterState.enduranceUnlocked = this.enduranceUnlocked
        newCharacterState.agility = this.agility
        newCharacterState.power = this.power
        newCharacterState.spirit = this.spirit
        newCharacterState.endurance = this.endurance

        return newCharacterState
    }
}