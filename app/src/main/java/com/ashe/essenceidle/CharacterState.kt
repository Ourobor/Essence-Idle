package com.ashe.essenceidle

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CharacterState() : RealmObject {
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