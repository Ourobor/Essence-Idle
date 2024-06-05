@file:OptIn(ExperimentalLayoutApi::class)

package com.ashe.essenceidle.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashe.essenceidle.model.maxActivities
import com.ashe.essenceidle.model.task.AgilityTrainingAction
import com.ashe.essenceidle.model.task.EnduranceTrainingAction
import com.ashe.essenceidle.model.task.EssenceAction
import com.ashe.essenceidle.model.task.MeditateEssenceAction
import com.ashe.essenceidle.model.task.PowerTrainingAction
import com.ashe.essenceidle.model.task.SpiritTrainingAction

@Composable
fun ActionDisplay(essenceActions: MutableList<EssenceAction>, queueFunction: (EssenceAction) -> Unit) {
    val fullOnActions = essenceActions.size >= maxActivities
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        modifier = Modifier.padding(3.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = "Action Engine",
                modifier = Modifier.padding(vertical = 5.dp),
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 5.dp))
            EssenceActionsList(essenceActions = essenceActions)
            FlowRow() {
                //list of all of the available actions to take
                Button(
                    onClick = { queueFunction(MeditateEssenceAction()) },
                    enabled = !fullOnActions
                ) {
                    Text(text = "Meditate")
                }

                Button(
                    onClick = { queueFunction(AgilityTrainingAction()) },
                    enabled = !fullOnActions
                ) {
                    Text(text = "Basic Agility Training")
                }

                Button(
                    onClick = { queueFunction(PowerTrainingAction()) },
                    enabled = !fullOnActions
                ) {
                    Text(text = "Basic Power Training")
                }

                Button(
                    onClick = { queueFunction(SpiritTrainingAction()) },
                    enabled = !fullOnActions
                ) {
                    Text(text = "Basic Spirit Training")
                }

                Button(
                    onClick = { queueFunction(EnduranceTrainingAction()) },
                    enabled = !fullOnActions
                ) {
                    Text(text = "Basic Endurance Training")
                }
                //Ex:
                //if(essenceActionUnlockedFlag){
                // Button("foo")
            }
        }
    }
}

//If I understand how jetpack compose works properly, by splitting ActionDisplay into two parts like
//this, I prevent the entire ActionDisplay from being recomposed if essenceActions changes
@Composable
fun EssenceActionsList(essenceActions: MutableList<EssenceAction>) {
    if (essenceActions.size > 0) {
        //Display the action summary
        essenceActions[0].Show()
    } else {
        //Display a fake action card
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            )
        ) {
            Column(Modifier.padding(5.dp)) {
                Text(fontSize = 20.sp, text = "No Action")
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    progress = { 0.0f },
                    strokeCap = StrokeCap.Round,
                )
            }
        }
    }
    Surface(color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.padding(3.dp)) {
        LazyColumn(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        ) {
            items(essenceActions) { action ->
                action.ShowCondensed()
            }
        }
    }
//    if (essenceActions.size > 1) {
//        FlowRow() {
//            //display the list of smaller actions queued
//            for (i in 1..<essenceActions.size) {
//                essenceActions[i].ShowCondensed()
//            }
//        }
//    }
}