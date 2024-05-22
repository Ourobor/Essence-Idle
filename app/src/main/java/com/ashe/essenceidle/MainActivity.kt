package com.ashe.essenceidle

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashe.essenceidle.ui.theme.EssenceIdleTheme
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    val viewModel: MainActivityViewModel by viewModels()
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
        }, 0, 500, TimeUnit.MILLISECONDS) // 1 second interval

        executor.scheduleAtFixedRate({
            viewModel.save()
        }, 0,30, TimeUnit.SECONDS)

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
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            text = "Essence Strength: ${characterState.essenceStrength}")
        MeditateButton(onClick = {
                viewModel.startMeditation()
            },
            ticksLeft = viewModel.meditationTicksLeft )
        if(characterState.soulForgeUnlocked) {
            Divider(
                modifier = Modifier.padding(10.dp)
            )
            SoulForge()
        }
    }
}

@Composable
fun SoulForge(){
    Text(
        modifier = Modifier.padding(start = 10.dp),
        text = "Soul Forge",
        color = MaterialTheme.colorScheme.primary,
        fontSize = 20.sp,
    )
}

@Composable
fun MeditateButton(onClick:  () -> Unit, ticksLeft: Int = 0) {
    Row() {
        Button(
            modifier = Modifier.padding(horizontal = 8.dp),
            onClick = onClick,
            enabled = ticksLeft == 0
        ) {
            Text(text = "Meditate")
        }

        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .align(Alignment.CenterVertically),
            //TODO: Remove magic number
            progress = (ticksLeft / 10.0).toFloat(),
            strokeCap = StrokeCap.Round
        )
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