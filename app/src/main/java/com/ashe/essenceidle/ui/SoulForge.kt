package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SoulForge(){
    Surface(
        color = MaterialTheme.colorScheme.surface
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "Soul Forge",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
            )
            HorizontalDivider(
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}
