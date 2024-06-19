package com.ashe.essenceidle.ui.components

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
fun StatDisplay(essenceStrength: Int, speed: Int, power: Int, spirit: Int, endurance: Int,
                speedUnlocked: Boolean, powerUnlocked: Boolean,
                spiritUnlocked: Boolean, enduranceUnlocked: Boolean) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.padding(3.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                modifier = Modifier
                    .fillMaxWidth().padding(bottom = 10.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
                text = "Essence Strength: $essenceStrength"
            )
            //Attributes
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        text = if(speedUnlocked) "Speed: ${speed / 100.0}" else "Speed: No data"
                    )

                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        text = if(spiritUnlocked) "Spirit: ${spirit / 100.0}" else "Spirit: No data"
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        text = if(powerUnlocked) "Power: ${power / 100.0}" else "Power: No data"
                    )

                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        text = if(enduranceUnlocked) "Endurance: ${endurance / 100.0}" else "Endurance: No data"
                    )
                }
            }
        }
    }
}