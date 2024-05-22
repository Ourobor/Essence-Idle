package com.ashe.essenceidle.model.task

import android.util.Log
import androidx.compose.runtime.Composable

private const val meditationTicks = 10

class MeditateEssenceAction() : EssenceAction() {
    init {
        ticksRemaining = meditationTicks
    }
    override fun progress(): Float {
        Log.d("EssenceIdle", "progress ${(ticksRemaining.toFloat() / meditationTicks.toFloat())}")
        return (ticksRemaining.toFloat() / meditationTicks)
    }

    @Composable
    override fun Show() {
        TODO("Not yet implemented")
    }

    @Composable
    override fun ShowCondensed() {
        TODO("Not yet implemented")
    }
}