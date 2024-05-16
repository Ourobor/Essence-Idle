package com.ashe.essenceidle

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class CharacterState {
    var essenceStrength by mutableStateOf(0)
    val meditationTicks by mutableStateOf(10)
    var meditationTicksLeft by mutableStateOf(0)

    fun doTick(){
        //Handle Meditation Tasks
        if (meditationTicksLeft > 1){
            meditationTicksLeft--;
        }
        else if (meditationTicksLeft == 1){
            meditationTicksLeft = 0
            essenceStrength += 10
        }
    }
}