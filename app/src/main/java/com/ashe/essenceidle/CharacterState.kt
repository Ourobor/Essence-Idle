package com.ashe.essenceidle

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CharacterState : RealmObject {
    @PrimaryKey
    var _id: ObjectId = ObjectId()
    var essenceStrength: Int = 0
    var soulForgeUnlocked: Boolean = false

    //TODO Check in on this to make sure there isn't a better way
    fun clone(): CharacterState {
        val newCharacterState = CharacterState()

        newCharacterState._id = this._id
        newCharacterState.essenceStrength = this.essenceStrength
        newCharacterState.soulForgeUnlocked = this.soulForgeUnlocked

        return newCharacterState
    }
}