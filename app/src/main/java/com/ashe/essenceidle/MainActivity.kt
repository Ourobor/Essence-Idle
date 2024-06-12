package com.ashe.essenceidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.MailOutline
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.model.data.CharacterState
import com.ashe.essenceidle.model.task.MeditateEssenceAction
import com.ashe.essenceidle.ui.ActionDisplay
import com.ashe.essenceidle.ui.SoulForge
import com.ashe.essenceidle.ui.StatDisplay
import com.ashe.essenceidle.ui.theme.EssenceIdleTheme
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

enum class Screens() {
    Home,
    Contacts
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onPause() {
        //attempt to save when the app is paused
        viewModel.save()
        super.onPause()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Create and launch periodic events
        val executor = Executors.newScheduledThreadPool(2)
        //Tick handler
        executor.scheduleAtFixedRate({
            //Do tick update
            viewModel.doTick()
        }, 0, 750, TimeUnit.MILLISECONDS)
        //Quicksave Handler
        executor.scheduleAtFixedRate({
            viewModel.save()
        }, 0, 120, TimeUnit.SECONDS)

        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val navController = rememberNavController()
            val characterState by viewModel.characterState.collectAsState()

            val navGraph = navController.createGraph(startDestination = Screens.Home.name) {
                composable(Screens.Home.name) { MainScreen(characterState = characterState) }
                composable(Screens.Contacts.name) { Text("Potato") }
            }

            EssenceIdleTheme() {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()

                        ModalDrawerSheet {
                            IconToggleButton(
                                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                                checked = false,
                                onCheckedChange = {
                                    scope.launch {
                                        drawerState.apply {
                                            if (isClosed) open() else close()
                                        }
                                    }
                                }) {
                                Icon(
                                    imageVector = Icons.Sharp.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                            NavigationDrawerItem(
                                label = { Text("Home") },
                                icon = {
                                       Icon(imageVector = Icons.Sharp.Home,
                                           contentDescription = "Home")
                                },
                                selected = navBackStackEntry?.destination?.route == Screens.Home.name,
                                shape = RectangleShape,
                                onClick = {
                                    navController.navigate(Screens.Home.name)
                                    scope.launch {
                                        drawerState.apply {
                                            close()
                                        }
                                    }
                                })
                            NavigationDrawerItem(
                                label = { Text("Contacts") },
                                icon = {
                                    Icon(imageVector = Icons.Sharp.MailOutline,
                                        contentDescription = "Contacts")
                                },
                                selected = navBackStackEntry?.destination?.route == Screens.Contacts.name,
                                shape = RectangleShape,
                                onClick = {
                                    navController.navigate(Screens.Contacts.name)
                                    scope.launch {
                                        drawerState.apply {
                                            close()
                                        }
                                    }
                                })
                        }
                    }
                ){
                    BottomSheetScaffold(
                        //Eyeballed values to make the bottom sheet show just enough when peeking.
                        sheetPeekHeight = if (!characterState.multipleActionsUnlocked) 65.dp else 128.dp,
                        //We don't really care about the sheet being opened before multiple actions are
                        //unlocked
                        sheetSwipeEnabled = characterState.multipleActionsUnlocked,
                        sheetShape = RectangleShape,
                        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        sheetDragHandle = {
                            if (characterState.multipleActionsUnlocked)
                                HorizontalDivider(
                                    Modifier
                                        .width(50.dp)
                                        .padding(top = 10.dp),
                                    thickness = 3.dp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                        },
                        topBar = {
                            TopAppBar(
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    titleContentColor = MaterialTheme.colorScheme.primary
                                ),
                                navigationIcon = {
                                    IconToggleButton(
                                        checked = false,
                                        onCheckedChange = {
                                            scope.launch {
                                                drawerState.apply {
                                                    if (isClosed) open() else close()
                                                }
                                            }
                                        }) {
                                        Icon(
                                            imageVector = Icons.Sharp.Menu,
                                            contentDescription = "Menu"
                                        )
                                    }
                                },
                                title = {
                                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                                    Text("${navBackStackEntry?.destination?.route}")
                                })
                        },
                        sheetContent = {
                            ActionSheet(characterState = characterState)
                        }
                    ) { padding ->
                        Surface(
                            modifier = Modifier.fillMaxSize().padding(padding),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            NavHost(navController = navController, graph = navGraph)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun MainScreen(characterState: CharacterState){
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
        ) {
            //Stat Display
            //As I understand this is probably the best way to do this right now?
            //By passing these as all primitives it lets Compose to more intelligently
            //skip recomposition
            StatDisplay(
                essenceStrength = characterState.essenceStrength,
                speed = characterState.speed,
                power = characterState.power,
                spirit = characterState.spirit,
                endurance = characterState.endurance,
                speedUnlocked = characterState.speedUnlocked,
                powerUnlocked = characterState.powerUnlocked,
                spiritUnlocked = characterState.spiritUnlocked,
                enduranceUnlocked = characterState.enduranceUnlocked
            )

            //Soul Forge
            if (characterState.soulForgeUnlocked) {
                SoulForge(characterState, viewModel::update, viewModel.unlocks)
            }
        }
    }

    @Composable
    private fun ActionSheet(characterState: CharacterState, ){
        //Action Engine/Simple meditation handling
        if (characterState.multipleActionsUnlocked) {
            ActionDisplay(
                viewModel.essenceActions,
                viewModel::queueEssenceAction,
                characterState
            )
        } else {
            Surface(
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
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
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EssenceIdleTheme {
        val characterState = CharacterState()
        characterState.essenceStrength = 123
        Column {
            //MainElement(characterState = characterState)
            //SoulForge(viewModel)
        }
    }
}