package com.ashe.essenceidle.model

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

abstract class SoulUnlock(
    val essenceCost: Int,
    val title: String,
    val description: String
) {

    abstract fun unlock(characterState: CharacterState): CharacterState

    abstract fun isUnlocked(characterState: CharacterState): Boolean

    @Composable
    fun Show(viewModel: MainActivityViewModel){
        val characterState by viewModel.characterState.collectAsState()
        if(!isUnlocked(characterState)) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                onClick = { viewModel.update(::unlock) },
                enabled = characterState.essenceStrength >= 200
            ) {
                Column(Modifier.padding(5.dp)) {
                    Text(fontSize = 20.sp, text = "$title ($essenceCost Essence)")
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        text = description
                    )
                }
            }
        }
    }
}

class ActionEngineUnlock: SoulUnlock(essenceCost = 200, title = "Action Engine", description =
"Unlock the ability to take more complicated actions and queue them.") {
    override fun unlock(characterState: CharacterState): CharacterState {
        characterState.essenceStrength -= 200
        characterState.multipleActionsUnlocked = true
        return characterState
    }

    override fun isUnlocked(characterState: CharacterState): Boolean {
        return characterState.multipleActionsUnlocked
    }

}