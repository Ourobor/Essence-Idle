package com.ashe.essenceidle.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.model.action.NoAction
import com.ashe.essenceidle.model.maxActivities

@Composable
fun ActionSheet(viewModel: MainActivityViewModel, simple: Boolean = false){
    val characterState by viewModel.characterState.collectAsState()
    //Action Engine/Simple meditation handling
    if (!simple) {
        ActionDisplay(
            viewModel.essenceActions,
            viewModel::queueEssenceAction,
            characterState
        )
    } else {
        if(viewModel.essenceActions.size > 0) viewModel.essenceActions[0].Show()
        else NoAction().Show()
        HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
        ActionList(
            queueFunction = viewModel::queueEssenceAction,
            enabled = viewModel.essenceActions.size >= maxActivities,
            speedUnlocked = characterState.speedUnlocked,
            powerUnlocked = characterState.powerUnlocked,
            spiritUnlocked = characterState.spiritUnlocked,
            enduranceUnlocked = characterState.enduranceUnlocked)
//        Box(Modifier.padding(5.dp)) {
//            Row {
//                Button(
//                    modifier = Modifier.padding(horizontal = 8.dp),
//                    onClick = {
//                        viewModel.essenceActions.add(MeditateEssenceAction())
//                    },
//                    enabled = viewModel.essenceActions.size == 0
//                ) {
//                    Text(text = "Meditate")
//                }
//                LinearProgressIndicator(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(20.dp)
//                        .align(Alignment.CenterVertically),
//                    progress = {
//                        if (viewModel.essenceActions.size > 0) {
//                            viewModel.essenceActions[0].progress()
//                        } else {
//                            0.0f
//                        }
//                    },
//                    strokeCap = StrokeCap.Round
//                )
//            }
//        }
    }
}