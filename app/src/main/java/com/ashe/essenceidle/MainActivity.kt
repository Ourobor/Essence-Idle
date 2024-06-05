package com.ashe.essenceidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ashe.essenceidle.model.CharacterState
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.model.task.MeditateEssenceAction
import com.ashe.essenceidle.ui.ActionDisplay
import com.ashe.essenceidle.ui.SoulForge
import com.ashe.essenceidle.ui.StatDisplay
import com.ashe.essenceidle.ui.theme.EssenceIdleTheme
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Create and launch periodic events
        val executor = Executors.newScheduledThreadPool(2)
        //Tick handler
        executor.scheduleAtFixedRate({
            //Do tick update
            viewModel.doTick()
        }, 0, 500, TimeUnit.MILLISECONDS)
        //Quicksave Handler
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
                    val characterState by viewModel.characterState.collectAsState()
                    val essenceActions = viewModel.essenceActions

                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        //Stat Display
                        //As I understand this is probably the best way to do this right now?
                        //By passing these as all primitives it lets Compose to more intelligently
                        //skip recomposition
                        StatDisplay(essenceStrength = characterState.essenceStrength,
                            agility = characterState.agility,
                            power = characterState.power,
                            spirit = characterState.spirit,
                            endurance = characterState.endurance,
                            agilityUnlocked = characterState.agilityUnlocked,
                            powerUnlocked = characterState.powerUnlocked,
                            spiritUnlocked = characterState.spiritUnlocked,
                            enduranceUnlocked = characterState.enduranceUnlocked)

                        //Action Engine/Simple meditation handling
                        if (characterState.multipleActionsUnlocked){
                            ActionDisplay(essenceActions, viewModel::queueEssenceAction)
                        }
                        else{
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                modifier = Modifier.padding(3.dp)) {
                                Box(Modifier.padding(5.dp)) {
                                    Row {
                                        Button(
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            onClick = {
                                                essenceActions.add(MeditateEssenceAction())
                                            },
                                            enabled = essenceActions.size == 0
                                        ) {
                                            Text(text = "Meditate")
                                        }
                                        LinearProgressIndicator(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(20.dp)
                                                .align(Alignment.CenterVertically),
                                            progress = {
                                                if (essenceActions.size > 0) {
                                                    essenceActions[0].progress()
                                                } else {
                                                    0.0f
                                                }
                                            },
                                            strokeCap = StrokeCap.Round
                                        )
                                    }
                                }
                            }
                        }

                        //Soul Forge
                        if (characterState.soulForgeUnlocked) {
                            SoulForge(characterState, viewModel::update, viewModel.unlocks)
                        }
                    }
                }
            }
        }
    }

    override fun onPause() {
        //attempt to save when the app is paused
        viewModel.save()
        super.onPause()
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
            //SoulForge(viewModel)
        }
    }
}