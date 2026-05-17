package dev.lamm.pennydrop

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.preference.PreferenceManager
import dev.lamm.pennydrop.screens.AboutScreen
import dev.lamm.pennydrop.screens.GameScreen
import dev.lamm.pennydrop.screens.PickPlayersScreen
import dev.lamm.pennydrop.screens.RankingsScreen
import dev.lamm.pennydrop.screens.SettingsScreen
import dev.lamm.pennydrop.ui.theme.PennyDropTheme
import dev.lamm.pennydrop.viewmodels.GameViewModel
import dev.lamm.pennydrop.viewmodels.PickPlayersViewModel
import dev.lamm.pennydrop.viewmodels.RankingsViewModel
import kotlinx.coroutines.launch

private object Routes {
    const val PICK_PLAYERS = "pickPlayers"
    const val GAME = "game"
    const val RANKINGS = "rankings"
    const val SETTINGS = "settings"
    const val ABOUT = "about"
}

private data class TopLevelDest(
    val route: String,
    val labelRes: Int,
    val iconRes: Int
)

private val topLevelDestinations = listOf(
    TopLevelDest(Routes.PICK_PLAYERS, R.string.players, R.drawable.ic_baseline_face_24),
    TopLevelDest(Routes.GAME, R.string.game, R.drawable.mdi_dice_6_black_24dp),
    TopLevelDest(Routes.RANKINGS, R.string.rankings, R.drawable.ic_baseline_format_list_numbered_24)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PennyDropApp() {
    PennyDropTheme {
        PennyDropScaffold()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PennyDropScaffold() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(titleResFor(currentRoute))) },
                navigationIcon = {
                    if (currentRoute == Routes.SETTINGS || currentRoute == Routes.ABOUT) {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                },
                actions = {
                    if (currentRoute in topLevelDestinations.map { it.route }) {
                        IconButton(onClick = { navController.navigate(Routes.SETTINGS) }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_baseline_settings_24),
                                contentDescription = stringResource(R.string.settings)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        bottomBar = {
            NavigationBar {
                topLevelDestinations.forEach { dest ->
                    val selected = backStackEntry?.destination?.hierarchy
                        ?.any { it.route == dest.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                painter = painterResource(dest.iconRes),
                                contentDescription = null
                            )
                        },
                        label = { Text(text = stringResource(dest.labelRes)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as ComponentActivity

    NavHost(
        navController = navController,
        startDestination = Routes.PICK_PLAYERS,
        modifier = modifier
    ) {
        composable(Routes.PICK_PLAYERS) {
            val pickPlayersViewModel: PickPlayersViewModel = hiltViewModel(viewModelStoreOwner = activity)
            val gameViewModel: GameViewModel = hiltViewModel(viewModelStoreOwner = activity)
            val players by pickPlayersViewModel.players.collectAsStateWithLifecycle()
            val scope = androidx.compose.runtime.rememberCoroutineScope()

            PickPlayersScreen(
                players = players,
                onUpdatePlayer = pickPlayersViewModel::updatePlayer,
                onPlayClicked = {
                    scope.launch {
                        gameViewModel.startGame(
                            pickPlayersViewModel.players.value
                                .filter { it.isIncluded }
                                .map { it.toPlayer() }
                        )
                        navController.navigate(Routes.GAME) {
                            popUpTo(Routes.PICK_PLAYERS) { saveState = true }
                            launchSingleTop = true
                        }
                    }
                }
            )
        }

        composable(Routes.GAME) {
            val gameViewModel: GameViewModel = hiltViewModel(viewModelStoreOwner = activity)
            val slots by gameViewModel.slots.collectAsStateWithLifecycle()
            val currentPlayer by gameViewModel.currentPlayer.collectAsStateWithLifecycle()
            val currentGame by gameViewModel.currentGame.collectAsStateWithLifecycle()
            val standings by gameViewModel.currentStandingsText.collectAsStateWithLifecycle()
            val canRoll by gameViewModel.canRoll.collectAsStateWithLifecycle()
            val canPass by gameViewModel.canPass.collectAsStateWithLifecycle()

            GameScreen(
                slots = slots,
                currentPlayerName = currentPlayer?.playerName
                    ?: stringResource(R.string.na),
                coinsLeft = currentPlayer?.pennies ?: 0,
                turnInfo = currentGame?.game?.currentTurnText.orEmpty(),
                standings = standings.orEmpty(),
                canRoll = canRoll,
                canPass = canPass,
                onRoll = gameViewModel::roll,
                onPass = gameViewModel::pass
            )
        }

        composable(Routes.RANKINGS) {
            val rankingsViewModel: RankingsViewModel = hiltViewModel(viewModelStoreOwner = activity)
            val summaries by rankingsViewModel.playerSummaries.collectAsStateWithLifecycle()
            RankingsScreen(summaries = summaries)
        }

        composable(Routes.SETTINGS) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
            SettingsScreen(
                prefs = prefs,
                onThemeModeChanged = { value ->
                    val nightMode = when (value) {
                        "Light" -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
                        "Dark" -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
                        else -> androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    }
                    androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode(nightMode)
                },
                onCreditsClicked = { navController.navigate(Routes.ABOUT) }
            )
        }

        composable(Routes.ABOUT) {
            AboutScreen()
        }
    }
}

private fun titleResFor(route: String?): Int = when (route) {
    Routes.PICK_PLAYERS -> R.string.pick_players
    Routes.GAME -> R.string.game
    Routes.RANKINGS -> R.string.rankings
    Routes.SETTINGS -> R.string.settings
    Routes.ABOUT -> R.string.about_penny_drop
    else -> R.string.app_name
}
