package com.ashe.essenceidle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Home
import androidx.compose.material.icons.sharp.MailOutline
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material.icons.sharp.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.createGraph
import com.ashe.essenceidle.model.MainActivityViewModel
import com.ashe.essenceidle.model.data.CharacterState
import com.ashe.essenceidle.ui.ActionSheet
import com.ashe.essenceidle.ui.screens.ContactScreen
import com.ashe.essenceidle.ui.screens.MainScreen
import com.ashe.essenceidle.ui.theme.EssenceIdleTheme
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

enum class Screens() {
    Home,
    Contacts,
    Settings
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
                composable(Screens.Home.name) { MainScreen(viewModel) }
                composable(Screens.Contacts.name) { ContactScreen() }
                composable(Screens.Settings.name) { Text("Settings!") }
            }

            EssenceIdleTheme() {
                ModalNavigationDrawer(
                    drawerState = drawerState,
                    scrimColor = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.7f),
                    drawerContent = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        ModalDrawerSheet {
//                            IconToggleButton(
//                                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
//                                checked = false,
//                                onCheckedChange = {
//                                    scope.launch {
//                                        drawerState.apply {
//                                            if (isClosed) open() else close()
//                                        }
//                                    }
//                                }) {
//                                Icon(
//                                    imageVector = Icons.Sharp.Menu,
//                                    contentDescription = "Menu"
//                                )
//                            }
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
                            NavigationDrawerItem(
                                label = { Text(Screens.Settings.name) },
                                icon = {
                                    Icon(imageVector = Icons.Sharp.Settings,
                                        contentDescription = Screens.Settings.name)
                                },
                                selected = navBackStackEntry?.destination?.route == Screens.Settings.name,
                                shape = RectangleShape,
                                onClick = {
                                    navController.navigate(Screens.Settings.name)
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
                        sheetContainerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
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
                            ActionSheet(viewModel = viewModel)
                        }
                    ) { padding ->
                        Surface(
                            modifier = Modifier
                                .padding(padding)
                                .fillMaxSize(),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            NavHost(navController = navController, graph = navGraph)
                        }
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