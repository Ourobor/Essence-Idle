package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.model.data.CharacterState
import com.ashe.essenceidle.model.task.MeditateEssenceAction

@Composable
fun ActionSheet(viewModel: MainActivityViewModel){
    val characterState by viewModel.characterState.collectAsState()
    //Action Engine/Simple meditation handling
    if (characterState.multipleActionsUnlocked) {
        ActionDisplay(
            viewModel.essenceActions,
            viewModel::queueEssenceAction,
            characterState
        )
    } else {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            modifier = Modifier.padding(3.dp)
        ) {
            Box(Modifier.padding(5.dp)) {
                Row {
                    Button(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        onClick = {
                            viewModel.essenceActions.add(MeditateEssenceAction())
                        },
                        enabled = viewModel.essenceActions.size == 0
                    ) {
                        Text(text = "Meditate")
                    }
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .align(Alignment.CenterVertically),
                        progress = {
                            if (viewModel.essenceActions.size > 0) {
                                viewModel.essenceActions[0].progress()
                            } else {
                                0.0f
                            }
                        },
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }
    }
}