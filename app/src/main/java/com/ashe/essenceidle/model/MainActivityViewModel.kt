package com.ashe.essenceidle.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ashe.essenceidle.model.task.EssenceAction
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.copyFromRealm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val maxActivities = 5
class MainActivityViewModel : ViewModel() {
    //The character state
    private val _characterState = MutableStateFlow(CharacterState())
    val characterState: StateFlow<CharacterState> = _characterState.asStateFlow()
    //Realm database references
    private val config = RealmConfiguration.Builder(schema = setOf(CharacterState::class))
        .schemaVersion(3).build()
    private val realm: Realm = Realm.open(config)

    var essenceActions = mutableListOf<EssenceAction>()
    var unlocks = mutableListOf<SoulUnlock>()

    init {
        unlocks.add(ActionEngineUnlock())
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
                //_characterState.value.essenceStrength = 200
            }
        }
    }

    /**
     * Execute one tick of simulation, updating the internal character state
     */
    fun doTick(){
        _characterState.update {characterState ->
            Log.d("EssenceIdle", "essenceActions ${essenceActions.size}")
            val newState = characterState.clone()

            //Handle EssenceActions
            if(essenceActions.size > 0){
                essenceActions[0].doTick()
                if(essenceActions[0].isComplete()){
                    //TODO check to make sure this actually gets garbage collected?
                    val action = essenceActions.removeFirst()
                    action.executeAction(newState)
                }
            }

            //if the soulForge hasn't been unlocked. Save cycles by nesting the second check only if
            //the soul forge isn't already unlocked, otherwise don't bother checking
            if (!newState.soulForgeUnlocked){
                //check to see if the player has accumulated a decent amount of essence and unlock it
                if (newState.essenceStrength >= 50){
                    newState.soulForgeUnlocked = true
                }
            }
            return@update newState
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
                realm.writeBlocking {copyToRealm(characterState.apply { }, UpdatePolicy.ALL) }
            }
        }
    }

}