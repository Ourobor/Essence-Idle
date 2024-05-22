package com.ashe.essenceidle.model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class MainActivityViewModel : ViewModel() {
    //The character state
    private val _characterState = MutableStateFlow(CharacterState())
    val characterState: StateFlow<CharacterState> = _characterState.asStateFlow()

    //Realm database references
    private val config = RealmConfiguration.Builder(schema = setOf(CharacterState::class))
        .schemaVersion(2).build()
    private val realm: Realm = Realm.open(config)

    //How many ticks per meditation task
    private val meditationTicks = 10
    //How many ticks are left in the current meditation task
    var meditationTicksLeft by mutableIntStateOf(0)

    init {
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
            }
        }
    }

    //TODO Gonna replace this in a bit
    fun startMeditation(){
        _characterState.update { characterState ->
            val newState = characterState.clone()
            meditationTicksLeft = meditationTicks
            return@update newState
        }
    }

    /**
     * Execute one tick of simulation, updating the internal character state
     */
    fun doTick(){
        _characterState.update {characterState ->
            Log.d("EssenceIdle", "ticks left $meditationTicksLeft")
            val newState = characterState.clone()
            //Handle Meditation Tasks
            if (meditationTicksLeft > 0){
                meditationTicksLeft--
                //Check if we /just/ reached 0
                if(meditationTicksLeft == 0){
                    newState.essenceStrength += 10
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