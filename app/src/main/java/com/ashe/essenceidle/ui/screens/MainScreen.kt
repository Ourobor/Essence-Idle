package com.ashe.essenceidle.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.ui.SoulForge
import com.ashe.essenceidle.ui.StatDisplay

@Composable
fun MainScreen(viewModel: MainActivityViewModel){
    val characterState by viewModel.characterState.collectAsState()
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {
        //Stat Display
        //As I understand this is probably the best way to do this right now?
        //By passing these as all primitives it lets Compose to more intelligently
        //skip recomposition
        StatDisplay(
            essenceStrength = characterState.essenceStrength,
            speed = characterState.speed,
            power = characterState.power,
            spirit = characterState.spirit,
            endurance = characterState.endurance,
            speedUnlocked = characterState.speedUnlocked,
            powerUnlocked = characterState.powerUnlocked,
            spiritUnlocked = characterState.spiritUnlocked,
            enduranceUnlocked = characterState.enduranceUnlocked
        )

        SoulForge(characterState, viewModel::update, viewModel.unlocks)
    }
}