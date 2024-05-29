package com.ashe.essenceidle.model.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashe.essenceidle.model.CharacterState

private const val meditationTicks = 10
private const val meditateEssenceGain = 10

class MeditateEssenceAction() : EssenceAction() {
    init {
        ticksRemaining = meditationTicks
    }

    override fun executeAction(characterState: CharacterState){
        characterState.essenceStrength += meditateEssenceGain
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / meditationTicks)
    }

    @Composable
    override fun Show() {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            )
        ) {
            Column(Modifier.padding(5.dp)) {
                Text(fontSize = 20.sp, text = "Meditating...")
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    progress = { progress() },
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }

    @Composable
    override fun ShowCondensed() {
        InputChip(selected = true,
            onClick = { /*TODO: Delete this action*/ },
            label = { Text("Meditate") },
            trailingIcon = {
                Icon(Icons.Filled.Clear,contentDescription = null)
            })
    }
}