package com.ashe.essenceidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ashe.essenceidle.model.CharacterState
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.ui.MeditateButton
import com.ashe.essenceidle.ui.SoulForge
import com.ashe.essenceidle.ui.StatDisplay
import com.ashe.essenceidle.ui.theme.EssenceIdleTheme
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onPause() {
        viewModel.save()
        super.onPause()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val executor = Executors.newScheduledThreadPool(2)

        executor.scheduleAtFixedRate({
            //Do tick update
            viewModel.doTick()
        }, 0, 500, TimeUnit.MILLISECONDS)

        executor.scheduleAtFixedRate({
            viewModel.save()
        }, 0,360, TimeUnit.SECONDS)

        setContent {
            //TODO: Update the theme. Manually setting it to always be darkmode is dumb.
            EssenceIdleTheme (darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainElement(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainElement(viewModel: MainActivityViewModel) {
    val characterState by viewModel.characterState.collectAsState()
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        StatDisplay(essenceStrength = characterState.essenceStrength)

        MeditateButton(onClick = {
                viewModel.startMeditation()
            },
            ticksLeft = viewModel.meditationTicksLeft )

        if(characterState.soulForgeUnlocked) {
            HorizontalDivider(
                modifier = Modifier.padding(10.dp)
            )
            SoulForge()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EssenceIdleTheme {
        val characterState = CharacterState()
        characterState.essenceStrength = 123
        Column {
            //MainElement(characterState = characterState)
            MeditateButton(onClick = {}, ticksLeft = 3)
            SoulForge()
        }
    }
}