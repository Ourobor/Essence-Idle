package com.ashe.essenceidle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class CharacterState {
    var essenceStrength by mutableStateOf(0)
    val meditationTicks by mutableStateOf(10)
    var meditationTicksLeft by mutableStateOf(0)
    val flags = Flags()

    fun doTick(){
        //Handle Meditation Tasks
        if (meditationTicksLeft > 0){
            meditationTicksLeft--

            //Check if we /just/ reached 0
            if(meditationTicksLeft == 0){
                essenceStrength += 10
            }
        }

        //if the soulForge hasn't been unlocked. Save cycles by nesting the second check only if
        //the soul forge isn't already unlocked, otherwise don't bother checking
        if (!flags.soulForgeUnlocked){
            //check to see if the player has accumulated a decent amount of essence and unlock it
            if (essenceStrength >= 50){
                flags.soulForgeUnlocked = true
            }
        }
    }
}

class Flags {
    var soulForgeUnlocked by mutableStateOf(false)
}