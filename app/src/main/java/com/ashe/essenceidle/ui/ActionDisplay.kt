@file:OptIn(ExperimentalLayoutApi::class)

package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashe.essenceidle.model.data.CharacterState
import com.ashe.essenceidle.model.maxActivities
import com.ashe.essenceidle.model.task.SpeedTrainingAction
import com.ashe.essenceidle.model.task.EnduranceTrainingAction
import com.ashe.essenceidle.model.task.EssenceAction
import com.ashe.essenceidle.model.task.MeditateEssenceAction
import com.ashe.essenceidle.model.task.NoAction
import com.ashe.essenceidle.model.task.PowerTrainingAction
import com.ashe.essenceidle.model.task.SpiritTrainingAction
import my.nanihadesuka.compose.LazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings

@Composable
fun ActionDisplay(essenceActions: MutableList<EssenceAction>, queueFunction: (EssenceAction) -> Unit,
                  characterState: CharacterState
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
        modifier = Modifier.padding(3.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Action Engine(${essenceActions.size}/$maxActivities)",
                modifier = Modifier.padding(vertical = 2.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
            )
            if(essenceActions.size > 0) essenceActions[0].Show()
            else NoAction().Show()
            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
            ActionList(
                queueFunction = queueFunction,
                enabled = essenceActions.size >= maxActivities,
                speedUnlocked = characterState.speedUnlocked,
                powerUnlocked = characterState.powerUnlocked,
                spiritUnlocked = characterState.spiritUnlocked,
                enduranceUnlocked = characterState.enduranceUnlocked)
            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
            EssenceActionsList(essenceActions = essenceActions)
        }
    }
}

//If I understand how jetpack compose works properly, by splitting ActionDisplay into two parts like
//this, I prevent the entire ActionDisplay from being recomposed if essenceActions changes
@Composable
fun EssenceActionsList(essenceActions: MutableList<EssenceAction>) {
    Surface(color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.padding(bottom = 5.dp),
        shape = RoundedCornerShape(8.dp)
        ) {
        val listState = rememberLazyListState()
        LazyColumnScrollbar(
            state = listState,
            settings = ScrollbarSettings.Default,
            modifier = Modifier
                .height(450.dp),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth()
            ) {
                var darkColor = true
                itemsIndexed(essenceActions) { index, action ->
                    if (index == 0) {
                        //Display the action summary
                        //essenceActions[0].Show()
                    } else {
                        action.ShowCondensed(darkColor)
                        darkColor = !darkColor
                    }
                }
                if(essenceActions.size < 2){
                    item { Text("No Actions Queued",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(3.dp)) }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ActionList(queueFunction: (EssenceAction) -> Unit, enabled: Boolean = false,
               speedUnlocked: Boolean,
               powerUnlocked: Boolean,
               spiritUnlocked: Boolean,
               enduranceUnlocked: Boolean) {

    FlowRow() {
        //list of all of the available actions to take
        Button(
            onClick = { queueFunction(MeditateEssenceAction()) },
            enabled = !enabled
        ) {
            Text(text = "Meditate")
        }
        if (speedUnlocked) Button(
            onClick = { queueFunction(SpeedTrainingAction()) },
            enabled = !enabled
        ) {
            Text(text = "Basic Speed Training")
        }

        if(powerUnlocked) Button(
            onClick = { queueFunction(PowerTrainingAction()) },
            enabled = !enabled
        ) {
            Text(text = "Basic Power Training")
        }

        if(spiritUnlocked) Button(
            onClick = { queueFunction(SpiritTrainingAction()) },
            enabled = !enabled
        ) {
            Text(text = "Basic Spirit Training")
        }

        if(enduranceUnlocked) Button(
            onClick = { queueFunction(EnduranceTrainingAction()) },
            enabled = !enabled
        ) {
            Text(text = "Basic Endurance Training")
        }
        //Ex:
        //if(essenceActionUnlockedFlag){
        // Button("foo")
    }
}