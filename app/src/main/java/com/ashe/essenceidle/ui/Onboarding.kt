package com.ashe.essenceidle.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import androidx.navigation.navArgument
import com.ashe.essenceidle.R
import com.ashe.essenceidle.model.MainActivityViewModel
import kotlin.concurrent.schedule

enum class Steps(val func : @Composable (viewModel: MainActivityViewModel, navController: NavController) -> Unit = {viewModel, navController -> }) {
    One({ viewModel, navController ->
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val image = painterResource(R.drawable.essenceicon)
            Image(painter = image, contentDescription = "Essence Icon",
                modifier = Modifier.padding(top = 60.dp, bottom = 30.dp).height(240.dp).fillMaxWidth())
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                fontSize = 30.sp,
                text = "Welcome"
            )
            Text(
                modifier = Modifier.padding(horizontal = 15.dp).padding(bottom = 20.dp),
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                lineHeight = 20.sp,
                text = "You're only a few steps away from the next step in communication within The Commission.",)
            Spacer(modifier = Modifier.weight(0.9f))
            Button(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp).fillMaxWidth(),
                shape = RectangleShape,
                onClick = { navController.navigate("onboarding/1") }
            ) {
                Text(text = "Next")
            }
        }
    }),
    Two({ viewModel, navController ->
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val image = painterResource(R.drawable.lotusflower)

            Image(
                painter = image, contentDescription = "Essence Icon",
                modifier = Modifier.padding(top = 60.dp, bottom = 30.dp)
                    .height(160.dp).fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                ),
                fontSize = 30.sp,
                text = "Let's get attuned"
            )
            Text(
                modifier = Modifier.padding(horizontal = 15.dp)
                    .padding(bottom = 20.dp),
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                lineHeight = 20.sp,
                text = "In order for Essence to interact with your soul, it needs to calibrate to your aura signature.\n \n Clear your mind, begin meditating and hit Next"
            )

            Spacer(modifier = Modifier.weight(0.9f))
            Button(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp).fillMaxWidth(),
                shape = RectangleShape,
                onClick = {
                        navController.navigate("onboarding/2")
                }
            ) {
                Text(text = "Attune")
            }
        }
    }),
    Three({ viewModel, navController ->
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val image = painterResource(R.drawable.lotusflower)
            var isAttuned by remember { mutableStateOf(false) }
            java.util.Timer().schedule(4000) {
                isAttuned = true
            }

            Image(painter = image, contentDescription = "Essence Icon",
                modifier = Modifier.padding(top = 60.dp, bottom = 30.dp).height(160.dp).fillMaxWidth())
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                fontSize = 30.sp,
                text = if(isAttuned) "Attuned!" else "Attuning..."
            )
            if(isAttuned){
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(10.dp),
                    strokeCap = StrokeCap.Round,
                    progress = { 1f }
                )
            }
            else {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(10.dp),
                    strokeCap = StrokeCap.Round
                )
            }
            Spacer(modifier = Modifier.weight(0.9f))
            Button(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp).fillMaxWidth(),
                shape = RectangleShape,
                enabled = isAttuned,
                onClick = { navController.navigate("onboarding/3") }
            ) {
                Text(text = "Next")
            }
        }
    }),
    Four({ viewModel, navController ->
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val image = painterResource(R.drawable.lotusflower)
            val characterState by viewModel.characterState.collectAsState()

            Image(
                painter = image, contentDescription = "Essence Icon",
                modifier = Modifier.padding(top = 60.dp, bottom = 30.dp)
                    .height(160.dp).fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                ),
                fontSize = 30.sp,
                text = "Test the connection"
            )
            Text(
                modifier = Modifier.padding(horizontal = 15.dp)
                    .padding(bottom = 20.dp),
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                lineHeight = 20.sp,
                text = "Now that Essence is attuned to your soul, let's test the connection. Give scheduled meditation a try!"
            )
            if(characterState.essenceStrength < 40) {
                Column(modifier = Modifier.padding(10.dp)){
                    ActionSheet(viewModel, true)
                }
            }
            Text(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                ),
                fontSize = 20.sp,
                text = if(characterState.essenceStrength > 10 && characterState.essenceStrength < 40)
                    "Good. Meditate ${(40 - characterState.essenceStrength)/10} more times!"
                else if(characterState.essenceStrength >= 40)
                    "Great! Link successfully created!"
                else
                    ""
            )
            Spacer(modifier = Modifier.weight(0.9f))
            Button(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp).fillMaxWidth(),
                shape = RectangleShape,
                enabled = characterState.essenceStrength >= 40,
                onClick = {
                    navController.navigate("onboarding/4")
                }
            ) {
                Text(text = "Next")
            }
        }
    }),
    Five({ viewModel, navController ->
        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            val image = painterResource(R.drawable.essenceicon)

            Image(
                painter = image, contentDescription = "Essence Icon",
                modifier = Modifier.padding(top = 60.dp, bottom = 30.dp)
                    .height(240.dp).fillMaxWidth()
            )
            Spacer(modifier = Modifier.weight(0.1f))
            Text(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 10.dp
                ),
                fontSize = 30.sp,
                text = "You're all set up!"
            )
            Text(
                modifier = Modifier.padding(horizontal = 15.dp)
                    .padding(bottom = 20.dp),
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                lineHeight = 20.sp,
                text = "Essence is connected and operational! Refer to your documentation for more information about what Essence can do for you!"
            )

            Spacer(modifier = Modifier.weight(0.9f))
            Button(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp).fillMaxWidth(),
                shape = RectangleShape,
                onClick = {
                    viewModel.update { characterState ->
                        val newState = characterState.clone()
                        newState.onboarded = true
                        newState
                    }
                }
            ) {
                Text(text = "Done!")
            }
        }
    })
}

@Composable
fun OnboardingSequence(viewModel: MainActivityViewModel) {
    val navController = rememberNavController()
    val currentStep: Int = navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.arguments?.getInt("step") ?: 0
    val navGraph = navController.createGraph(startDestination = "onboarding/{step}") {
        composable("onboarding/{step}",
            arguments = listOf(navArgument("step") { type = NavType.IntType }),
            enterTransition = {
                slideIntoContainer(
                    animationSpec = tween(200, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    animationSpec = tween(200, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(200, easing = EaseIn),
                    towards = AnimatedContentTransitionScope.SlideDirection.Start
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    animationSpec = tween(200, easing = EaseOut),
                    towards = AnimatedContentTransitionScope.SlideDirection.End
                )
            }
        ) {
            Steps.entries[it.arguments?.getInt("step") ?: 0].func(viewModel, navController)
        }
    }
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surfaceContainer) {
        Column {
            NavHost(
                navController = navController,
                graph = navGraph,
                modifier = Modifier.weight(1f)
            )
            LinearProgressIndicator(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .height(10.dp),
                strokeCap = StrokeCap.Round,
                progress = { (currentStep + 1.0f) / (Steps.entries.size) })
        }
    }
}
