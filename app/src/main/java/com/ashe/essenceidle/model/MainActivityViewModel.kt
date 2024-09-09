package com.ashe.essenceidle.model

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashe.essenceidle.model.action.EssenceAction
import com.ashe.essenceidle.model.contact.ContactScript
import com.ashe.essenceidle.model.contact.WatchfulRaven
import com.ashe.essenceidle.model.database.CharacterState
import com.ashe.essenceidle.model.database.ContactRecord
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val maxActivities = 15
class MainActivityViewModel : ViewModel() {
    //The character state
    private val _characterState = MutableStateFlow(CharacterState())
    val characterState: StateFlow<CharacterState> = _characterState.asStateFlow()
    //Realm database references
    private val charConfig = RealmConfiguration.Builder(schema = setOf(CharacterState::class, ContactRecord::class))
        .schemaVersion(18).build()
    private val realm: Realm = Realm.open(charConfig)

    val essenceActions = mutableStateListOf<EssenceAction>()
    //Default Contacts go here. These will be overwritten in the init method if there is contact
    //data in the database
    var contacts = mutableStateMapOf<String, ContactScript>(
        Pair("WR", WatchfulRaven(listOf(), WatchfulRaven.Steps.UNINTRODUCED))
    )
    var unlocks = mutableStateListOf<SoulUnlock>()

    init {
        unlocks.add(SpeedUnlock())
        unlocks.add(PowerUnlock())
        unlocks.add(SpiritUnlock())
        unlocks.add(EnduranceUnlock())
        viewModelScope.launch {
            val items: RealmResults<CharacterState> = realm.query<CharacterState>().find()
            if(items.size == 0){
                realm.writeBlocking {
                    copyToRealm(CharacterState().apply {

                    })
                }
            }
            else {
                _characterState.value = items[0].copyFromRealm()

                //unpack contacts
                Log.d("EssIdle","Beginning Contact Unpacking")
                for(contactRecord in _characterState.value._contacts){
                    val newContact = createContact(contactRecord)
                    if(newContact != null) {
                        Log.d("EssIdle", "New Contact Registered: ${newContact.id}, Currently: ${newContact.currentStep}, Previously: ${newContact.previousSteps}")
                        contacts[newContact.id] = newContact
                    }
                }

                /*
                    Debugging Overrides, I'm leaving them commented 'cuz it's easier to uncomment and
                    modify than write them from scratch every time
                 */

//                contacts["WR"] = WatchfulRaven(listOf(WatchfulRaven.Steps.UNINTRODUCED), WatchfulRaven.Steps.INTRODUCED)

//                    update { val char = CharacterState(); char._id = _characterState.value._id;char}
//                    _characterState.value.speed = 100
//                    _characterState.value.power = 100
//                    _characterState.value.spirit = 100
//                    _characterState.value.endurance = 100
//                    _characterState.value.essenceStrength = 200
//                    _characterState.value.multipleActionsUnlocked = true
            }
        }
    }

    /**
     * Execute one tick of simulation, updating the internal character state
     */
    fun doTick(){
        //Handle EssenceActions
        if(essenceActions.size > 0){
            essenceActions[0].doTick()
            if(essenceActions[0].isComplete()){
                //TODO check to make sure this actually gets garbage collected?
                val action = essenceActions.removeFirst()

                //Actual State update
                _characterState.update {characterState ->
                    val newState = characterState.clone()
                    action.executeAction(newState)
                    return@update newState
                }
            }
        }

        //Update Contact state
        for(contact in contacts){
            val contactStep = contact.value.currentStep
            if(contactStep.readyForProgression(characterState.value)){
                val nextStep = contactStep.nextStep()
                if(nextStep != null){
                    contacts[contact.value.id] = contact.value.takeStep(nextStep)
                }
            }
        }
    }

    /**
     * queues up an essence action at the end of the essence action queue
     * @param action The action to add to the end of the list
     */
    fun queueEssenceAction(action: EssenceAction){
        if(essenceActions.size < maxActivities) {
            essenceActions.add(action)
        }
    }

    /**
     * Executes an update function on the character state in order to update it properly. Updates
     * the state to what the update function returns
     * @param updateFunction an update function that takes in a clone of the current state and returns it back after modification.
     */
    fun update(updateFunction: (CharacterState) -> CharacterState) {
        _characterState.update { characterState ->
            val newState = characterState.clone()
            updateFunction(newState)
        }
    }

    /**
     * Saves the current state of the character to backing database
     */
    fun save(): Job {
        return viewModelScope.launch {
            _characterState.collectLatest { characterState ->
                //read contacts and put them into the character State
                for (contact in contacts){
                    //ensure contacts list is empty
                    characterState._contacts = realmListOf()

                    characterState._contacts.add(
                        ContactRecord(contact.value.id,
                            contact.value.currentStep.toString(),
                            contact.value.previousSteps))
                }
                realm.writeBlocking {copyToRealm(characterState.apply { }, UpdatePolicy.ALL) }
            }
        }
    }
    object Flags {
        fun getUserName(contacts: Map<String, ContactScript>): String {
            val step = contacts["WR"]?.currentStep
            return if(step is WatchfulRaven.Steps) {
                when (step) {
                    WatchfulRaven.Steps.UNINTRODUCED, WatchfulRaven.Steps.INTRODUCED -> "Unknown User"
                    WatchfulRaven.Steps.STAGETWO, WatchfulRaven.Steps.RITUALINTRODUCTION -> "Nobody, Basic user"
                }
            } else {
                "Error, Unknown User"
            }
        }

        fun soulForgeUnlocked(contacts: Map<String, ContactScript>): Boolean {
            val step = contacts["WR"]?.currentStep
            return if (step is WatchfulRaven.Steps) {
                when (step) {
                    WatchfulRaven.Steps.UNINTRODUCED, WatchfulRaven.Steps.INTRODUCED -> false
                    else -> true
                }
            }
            else{
                false
            }
        }

        fun ritualUnlocked(contacts: Map<String, ContactScript>): Boolean{
            val step = contacts["WR"]?.currentStep
            return if (step is WatchfulRaven.Steps) {
                when (step) {
                    WatchfulRaven.Steps.UNINTRODUCED, WatchfulRaven.Steps.INTRODUCED, WatchfulRaven.Steps.STAGETWO -> false
                    WatchfulRaven.Steps.RITUALINTRODUCTION -> true
                }
            }
            else{
                false
            }
        }
    }

}