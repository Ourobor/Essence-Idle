package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun MeditateButton(onClick:  () -> Unit, ticksLeft: Int = 0) {
    Row {
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
            progress = { (ticksLeft / 10.0).toFloat() },
            strokeCap = StrokeCap.Round
        )
    }
}