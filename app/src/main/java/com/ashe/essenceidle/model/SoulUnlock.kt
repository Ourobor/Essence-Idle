package com.ashe.essenceidle.model

import androidx.annotation.CallSuper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

abstract class SoulUnlock(
    val essenceCost: Int,
    val title: String,
    val description: String
) {

    /**
     * Not 100% sure this is ideal, but.. This (hopefully) ensures that all unlocks subtract their
     * cost from the current state before they do anything else
     */
    @CallSuper
    open fun unlock(characterState: CharacterState): CharacterState{
        characterState.essenceStrength - essenceCost
        return characterState
    }

    /**
     * Checks if this unlocked is already unlocked
     */
    abstract fun isUnlocked(characterState: CharacterState): Boolean


    @Composable
    fun Show(
        characterState: CharacterState,
        updateFunction: ((CharacterState) -> CharacterState) -> Unit
    ){
        if(!isUnlocked(characterState)) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                onClick = { updateFunction(::unlock) },
                enabled = characterState.essenceStrength >= essenceCost
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

class ActionEngineUnlock: SoulUnlock(essenceCost = 100, title = "Action Engine", description =
"Unlock the ability to take more complicated actions and queue them.") {
    override fun unlock(characterState: CharacterState): CharacterState {
        val newCharacterState = super.unlock(characterState)
        newCharacterState.multipleActionsUnlocked = true
        return newCharacterState
    }

    override fun isUnlocked(characterState: CharacterState): Boolean {
        return characterState.multipleActionsUnlocked
    }

}