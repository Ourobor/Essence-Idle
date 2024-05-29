package com.ashe.essenceidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ashe.essenceidle.model.CharacterState
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.ui.MainElement
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
                    MainElement(viewModel)
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