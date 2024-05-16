package com.ashe.essenceidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ashe.essenceidle.ui.theme.EssenceIdleTheme
import java.util.Timer
import java.util.TimerTask

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO Verify that this is a good idea?
        val charState = CharacterState()

        val timer = Timer()
        val updateTask = object : TimerTask() {
            override fun run() {
                charState.essenceStrength++
            }
        }
        timer.schedule(updateTask, 0, 750)

        setContent {
            EssenceIdleTheme (darkTheme = true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val mutableCharacterState = remember { charState }

                    Column {
                        Text(
                            modifier = Modifier.fillMaxWidth()
                                .padding(10.dp),
                            color = MaterialTheme.colorScheme.primary,
                            text = "Essence Strength: ${mutableCharacterState.essenceStrength}")
                        Button(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            onClick = { mutableCharacterState.essenceStrength++ }) {
                            Text(text = "Meditate")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EssenceIdleTheme {
    }
}