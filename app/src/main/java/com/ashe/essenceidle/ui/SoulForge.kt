package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashe.essenceidle.model.database.CharacterState
import com.ashe.essenceidle.model.SoulUnlock

@Composable
fun SoulForge(
    characterState: CharacterState,
    updateFunction: ((CharacterState) -> CharacterState) -> Unit,
    unlocks: SnapshotStateList<SoulUnlock>
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.padding(3.dp)
    ) {
        Column {
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = "Soul Forge",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                text = "Reforge your soul to unlock new powers")
            HorizontalDivider(
                modifier = Modifier.padding(10.dp)
            )
            for (unlock in unlocks){
                unlock.Show(characterState, updateFunction)
            }
        }
    }
}
