package com.ashe.essenceidle.model.database

import com.ashe.essenceidle.model.contact.ContactScript
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmList
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
    var essenceStrength: Int = 10
    var totalEssenceCollected: Int = 0
    var speed: Int = 100
    var power: Int = 100
    var spirit: Int = 100
    var endurance: Int = 100
    var onboarded: Boolean = false
    var speedUnlocked: Boolean = false
    var powerUnlocked: Boolean = false
    var spiritUnlocked: Boolean = false
    var enduranceUnlocked: Boolean = false

    //Custom managed attributes. These are managed by MainActivityViewModel and are only used when
    //the character state is either read from the database or when the app is saving. These attributes
    //do not get cloned and should be left the fuck alone. Do not muck with these unless you're
    //doing in MainActivityViewModel's save function or when it reads in the data.
    var _contacts: RealmList<ContactRecord> = realmListOf()

    //TODO Check in on this to make sure there isn't a better way
    fun clone(): CharacterState {
        val newCharacterState = CharacterState()

        newCharacterState._id = this._id
        newCharacterState.essenceStrength = this.essenceStrength
        newCharacterState.totalEssenceCollected = this.totalEssenceCollected
        newCharacterState.onboarded = this.onboarded
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

class ContactRecord(): EmbeddedRealmObject{
    var contactId: String = ""
    var currectStepId: String = ""
    var previousSteps: RealmList<String> = realmListOf()
    constructor(contactId: String, currectStepId: String, previousSteps: List<ContactScript.ScriptStep>) : this() {
        this.contactId = contactId
        this.currectStepId = currectStepId
        for(step in previousSteps) {
            this.previousSteps.add(step.toString())
        }
    }
}