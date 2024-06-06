package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatDisplay(essenceStrength: Int, agility: Int, power: Int, spirit: Int, endurance: Int,
                agilityUnlocked: Boolean, powerUnlocked: Boolean,
                spiritUnlocked: Boolean, enduranceUnlocked: Boolean) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.padding(3.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                text = "Essence Strength: $essenceStrength"
            )
            //Attributes
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround) {
                Column(horizontalAlignment = Alignment.End) {
                    if (agilityUnlocked) {
                        Text(
                            color = MaterialTheme.colorScheme.primary,
                            text = "Agility: ${agility / 100.0}"
                        )
                    }

                    if (spiritUnlocked) {
                        Text(
                            color = MaterialTheme.colorScheme.primary,
                            text = "Spirit: ${spirit / 100.0}"
                        )
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    if (powerUnlocked) {
                        Text(
                            color = MaterialTheme.colorScheme.primary,
                            text = "Power: ${power / 100.0}"
                        )
                    }

                    if (enduranceUnlocked) {
                        Text(
                            color = MaterialTheme.colorScheme.primary,
                            text = "Endurance: ${endurance / 100.0}"
                        )
                    }
                }
            }
        }
    }
}