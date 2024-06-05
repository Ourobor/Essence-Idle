package com.ashe.essenceidle.model.task

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashe.essenceidle.model.CharacterState

private const val totalAttributePenalty = 100.0
private const val thisAttributePenalty = 300.0
//TODO: This is probably should be in another file probably
fun calculateAttributeGain(totalAttributes: Int, thisAttribute: Int): Int {
    //Factors in order of importance
    //1: penalty the more total attribute points. As you get more attributes, it takes longer to
    //   get more
    val totalPenalty = 1.0 - (totalAttributes / (totalAttributes + totalAttributePenalty))

    //2: penalty the higher this attribute. A minor braking factor to subtly deincentivize
    //   min-maxing excessively
    val thisPenalty = 1.0 - (thisAttribute / (thisAttribute + thisAttributePenalty))
//    val thisPenalty = 1.0

    //3: penalty for pushing an attribute above x(~20)% of the total pool max by ~40%? Encourage
    //   the player to keep their attributes at a minimum value relative to their overall attributes
    val minMaxPenalty = if((thisAttribute / totalAttributes) > 0.2){
        0.5
    }
    else{
        1.0
    }
//    val minMaxPenalty = 1.0

    return (totalPenalty * thisPenalty * minMaxPenalty * 10.0).toInt()
}

private const val agilityTrainingTicks = 10
class AgilityTrainingAction: EssenceAction() {
    init {
        ticksRemaining = agilityTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        Log.d("EssenceIdle", "Agility:${characterState.agility}")
        val totalAttributes = (characterState.agility + characterState.power + characterState.endurance + characterState.spirit)
        characterState.agility +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.agility)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / agilityTrainingTicks)
    }

    @Composable
    override fun Show() {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            )
        ) {
            Column(Modifier.padding(5.dp)) {
                Text(fontSize = 20.sp, text = "Training your natural agility...")
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    progress = { progress() },
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }

    @Composable
    override fun ShowCondensed() {
        InputChip(selected = true,
            onClick = { /*TODO: Delete this action*/ },
            label = { Text("AGI Train") },
            trailingIcon = {
                Icon(Icons.Filled.Clear,contentDescription = null)
            })
    }
}

private const val powerTrainingTicks = 10
class PowerTrainingAction: EssenceAction() {
    init {
        ticksRemaining = powerTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        Log.d("EssenceIdle", "Agility:${characterState.agility}")
        val totalAttributes = (characterState.agility + characterState.power + characterState.endurance + characterState.spirit)
        characterState.power +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.power)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / powerTrainingTicks)
    }

    @Composable
    override fun Show() {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            )
        ) {
            Column(Modifier.padding(5.dp)) {
                Text(fontSize = 20.sp, text = "Training your natural power...")
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    progress = { progress() },
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }

    @Composable
    override fun ShowCondensed() {
        InputChip(selected = true,
            onClick = { /*TODO: Delete this action*/ },
            label = { Text("PWR Train") },
            trailingIcon = {
                Icon(Icons.Filled.Clear,contentDescription = null)
            })
    }
}

private const val spiritTrainingTicks = 10
class SpiritTrainingAction: EssenceAction() {
    init {
        ticksRemaining = spiritTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        Log.d("EssenceIdle", "Agility:${characterState.agility}")
        val totalAttributes = (characterState.agility + characterState.power + characterState.endurance + characterState.spirit)
        characterState.spirit +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.spirit)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / spiritTrainingTicks)
    }

    @Composable
    override fun Show() {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            )
        ) {
            Column(Modifier.padding(5.dp)) {
                Text(fontSize = 20.sp, text = "Training your natural spirit...")
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    progress = { progress() },
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }

    @Composable
    override fun ShowCondensed() {
        InputChip(selected = true,
            onClick = { /*TODO: Delete this action*/ },
            label = { Text("SPR Train") },
            trailingIcon = {
                Icon(Icons.Filled.Clear,contentDescription = null)
            })
    }
}

private const val enduranceTrainingTicks = 10
class EnduranceTrainingAction: EssenceAction() {
    init {
        ticksRemaining = enduranceTrainingTicks
    }

    override fun executeAction(characterState: CharacterState) {
        val totalAttributes = (characterState.agility + characterState.power + characterState.endurance + characterState.spirit)
        characterState.endurance +=
            calculateAttributeGain(
                totalAttributes = totalAttributes,
                thisAttribute = characterState.endurance)
    }

    override fun progress(): Float {
        return (ticksRemaining.toFloat() / enduranceTrainingTicks)
    }

    @Composable
    override fun Show() {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            )
        ) {
            Column(Modifier.padding(5.dp)) {
                Text(fontSize = 20.sp, text = "Training your natural endurance...")
                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 5.dp))
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(20.dp),
                    progress = { progress() },
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }

    @Composable
    override fun ShowCondensed() {
        InputChip(selected = true,
            onClick = { /*TODO: Delete this action*/ },
            label = { Text("END Train") },
            trailingIcon = {
                Icon(Icons.Filled.Clear,contentDescription = null)
            })
    }
}
