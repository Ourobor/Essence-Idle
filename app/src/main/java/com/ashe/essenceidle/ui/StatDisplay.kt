package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun StatDisplay(essenceStrength: Int) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp),
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        fontSize = 20.sp,
        text = "Essence Strength: $essenceStrength"
    )
}