package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashe.essenceidle.model.MainActivityViewModel

@Composable
fun MainElement(viewModel: MainActivityViewModel) {
    val characterState by viewModel.characterState.collectAsState()
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StatDisplay(essenceStrength = characterState.essenceStrength)

        MeditateButton(
            onClick = {
                viewModel.startMeditation()
            },
            ticksLeft = viewModel.meditationTicksLeft
        )

        if (characterState.soulForgeUnlocked) {
            HorizontalDivider(
                modifier = Modifier.padding(10.dp)
            )
            SoulForge()
        }
    }
}