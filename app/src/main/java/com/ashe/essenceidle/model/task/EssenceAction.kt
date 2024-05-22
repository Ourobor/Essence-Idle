package com.ashe.essenceidle.model.task

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

abstract class EssenceAction {
    /**
     * The number of ticks remaining. This must be used. Arbitrary duration EssenceActions are not
     * cool >:( (Also not currently intended or supported)
     */
    var ticksRemaining by mutableIntStateOf(0)
        protected set

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
    abstract fun Show()

    /**
     * A composable function to display a more condensed visual representation of this action. This
     * is primarily used in the action tracker to display this action when it isn't the current
     * action being executed
     */
    @Composable
    abstract fun ShowCondensed()
}