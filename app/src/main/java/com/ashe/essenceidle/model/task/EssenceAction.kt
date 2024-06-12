package com.ashe.essenceidle.model.task

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ashe.essenceidle.model.data.CharacterState

abstract class EssenceAction(
    private val fullDescription: String,
    private val shortDescription: String
) {
    /**
     * The number of ticks remaining. This must be used. Arbitrary duration EssenceActions are not
     * cool >:( (Also not currently intended or supported)
     */
    var ticksRemaining by mutableIntStateOf(0)
        protected set

    /**
     * Executes the action, modifying the state. This is intended to be executed in a coroutine,
     * namely the update atomic coroutine that the viewModel uses. We do a naughty and do side
     * effects on the given state
     *
     * @param characterState The current state
     */
    abstract fun executeAction(characterState: CharacterState)

    fun isComplete(): Boolean{
        return ticksRemaining == 0
    }

    /**
     * Handle the actions of a tick. Overwrite do extra stuff every tick
     */
    fun doTick(){
        ticksRemaining--
    }

    /**
     * Get the current progress of this action as a float
     * @return a value between 0.0 and 1.0 representing percentage of completion of this action
     */
    abstract fun progress(): Float

    /**
     * A composable function to display a visual representation of this action
     */
    @Composable
    fun Show(){
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(Modifier.padding(5.dp)) {
                Text(fontSize = 20.sp, text = fullDescription)
                HorizontalDivider(color = MaterialTheme.colorScheme.onSecondaryContainer,
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

    /**
     * A composable function to display a more condensed visual representation of this action. This
     * is primarily used in the action tracker to display this action when it isn't the current
     * action being executed
     */
    @Composable
    fun ShowCondensed(darkColor: Boolean = false) {
        Surface(color = if (darkColor){
            MaterialTheme.colorScheme.primaryContainer
        }else{
            MaterialTheme.colorScheme.secondaryContainer
        },
            modifier = Modifier.padding(horizontal = 0.dp).fillMaxWidth()) {
            Text(modifier = Modifier.padding(horizontal = 10.dp), text = shortDescription)
        }
    }
}

/**
 * No Action is a dummy class used to show that there is no action for use in the action engine.
 */
class NoAction: EssenceAction("No action", "This shouldn't be possible :3"){
    override fun executeAction(characterState: CharacterState) {
        throw RuntimeException("This is a dummy action and shouldn't actually be possible to execute")
    }

    override fun progress(): Float {
        return 0.0f
    }
}