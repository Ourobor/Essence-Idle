package com.ashe.essenceidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.remember
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Create the character state
        /*TODO Verify that using this reference as well as the later mutableCharacterState
           is good form */
        val charState = CharacterState()

        val executor = Executors.newScheduledThreadPool(1)

        executor.scheduleAtFixedRate({
            //Do tick update
            charState.doTick()
        }, 0, 500, TimeUnit.MILLISECONDS) // 1 second interval

        setContent {
            //TODO: Update the theme. Manually setting it to always be darkmode is dumb.
            EssenceIdleTheme (darkTheme = true) {
                //Create a mutable reference to charState
                val mutableCharState = remember { charState }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainElement(mutableCharState)
                }
            }
        }
    }
}

@Composable
fun MainElement(mutableCharState: CharacterState) {
    Column {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 15.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 20.sp,
            text = "Essence Strength: ${mutableCharState.essenceStrength}")
        MeditateButton(onClick = {
                mutableCharState.meditationTicksLeft = mutableCharState.meditationTicks
            },
            ticksLeft = mutableCharState.meditationTicksLeft )
        if(mutableCharState.flags.soulForgeUnlocked) {
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
            MainElement(mutableCharState = characterState)
            MeditateButton(onClick = {}, ticksLeft = 3)
            SoulForge()
        }
    }
}