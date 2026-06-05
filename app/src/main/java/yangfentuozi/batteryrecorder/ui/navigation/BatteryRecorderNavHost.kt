package yangfentuozi.batteryrecorder.ui.navigation

import android.net.Uri
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import yangfentuozi.batteryrecorder.R
import yangfentuozi.batteryrecorder.shared.data.BatteryStatus
import yangfentuozi.batteryrecorder.shared.data.RecordsFile
import yangfentuozi.batteryrecorder.ui.screens.history.HistoryListScreen
import yangfentuozi.batteryrecorder.ui.screens.history.RecordDetailScreen
import yangfentuozi.batteryrecorder.ui.screens.home.HomeScreen
import yangfentuozi.batteryrecorder.ui.screens.prediction.PredictionDetailScreen
import yangfentuozi.batteryrecorder.ui.screens.settings.SettingsScreen
import yangfentuozi.batteryrecorder.ui.viewmodel.HistorySharedViewModel
import yangfentuozi.batteryrecorder.ui.viewmodel.MainViewModel
import yangfentuozi.batteryrecorder.ui.viewmodel.SettingsViewModel

private const val ANIMATION_DURATION = 300
private const val SCALE_FACTOR = 0.95f

private val animationSpec = tween<Float>(
    durationMillis = ANIMATION_DURATION,
    easing = FastOutSlowInEasing
)

private val defaultEnterTransition: EnterTransition = scaleIn(
    initialScale = SCALE_FACTOR,
    animationSpec = animationSpec
) + fadeIn(animationSpec = animationSpec)

private val defaultExitTransition: ExitTransition = fadeOut(animationSpec = animationSpec)

private val defaultPopEnterTransition: EnterTransition = fadeIn(animationSpec = animationSpec)

private val defaultPopExitTransition: ExitTransition = scaleOut(
    targetScale = SCALE_FACTOR,
    animationSpec = animationSpec
) + fadeOut(animationSpec = animationSpec)

// ============================================
// Bottom Navigation Items
// ============================================
data class BottomNavItem(
    val route: String,
    val labelResId: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = NavRoute.Home.route,
        labelResId = R.string.nav_home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = NavRoute.History.route,
        labelResId = R.string.nav_history,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History
    ),
    BottomNavItem(
        route = NavRoute.Settings.route,
        labelResId = R.string.nav_settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings
    )
)

@Composable
fun BatteryRecorderMainScreen(
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val historyViewModel: HistorySharedViewModel = viewModel()

    Scaffold(
        modifier = modifier,
        bottomBar = {
            BatteryRecorderBottomBar(navController = navController)
        }
    ) { paddingValues ->
        BatteryRecorderNavHost(
            navController = navController,
            mainViewModel = mainViewModel,
            settingsViewModel = settingsViewModel,
            historyViewModel = historyViewModel,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
private fun BatteryRecorderBottomBar(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Only show bottom bar on main destinations
    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    if (showBottomBar) {
        NavigationBar {
            bottomNavItems.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        navController.navigate(item.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = stringResource(item.labelResId)
                        )
                    },
                    label = {
                        Text(text = stringResource(item.labelResId))
                    }
                )
            }
        }
    }
}

@Composable
fun BatteryRecorderNavHost(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    settingsViewModel: SettingsViewModel,
    historyViewModel: HistorySharedViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavRoute.Home.route,
        modifier = modifier
    ) {
        composable(
            route = NavRoute.Home.route,
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth / 4 }) +
                        fadeOut()
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth / 4 }) +
                        fadeIn()
            },
            enterTransition = { null },
            popExitTransition = { null }
        ) {
            HomeScreen(
                viewModel = mainViewModel,
                settingsViewModel = settingsViewModel,
                onNavigateToSettings = {
                    navController.navigate(NavRoute.Settings.route)
                },
                onNavigateToHistoryList = { type ->
                    navController.navigate(NavRoute.HistoryList.createRoute(type.dataDirName))
                },
                onNavigateToRecordDetail = { type, name ->
                    navController.navigate(
                        NavRoute.RecordDetail.createRoute(
                            type.dataDirName,
                            Uri.encode(name)
                        )
                    )
                },
                onNavigateToPredictionDetail = {
                    navController.navigate(NavRoute.PredictionDetail.route)
                }
            )
        }

        composable(
            route = NavRoute.History.route,
            exitTransition = {
                if (targetState.destination.route == NavRoute.RecordDetail.route) {
                    slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth / 4 }) +
                            fadeOut()
                } else {
                    defaultExitTransition
                }
            },
            popEnterTransition = {
                if (initialState.destination.route == NavRoute.RecordDetail.route) {
                    slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth / 4 }) +
                            fadeIn()
                } else {
                    defaultPopEnterTransition
                }
            },
            popExitTransition = { defaultPopExitTransition }
        ) {
            // History tab - default to Charging status
            HistoryListScreen(
                batteryStatus = BatteryStatus.Charging,
                viewModel = historyViewModel,
                onNavigateToRecordDetail = { type, name ->
                    navController.navigate(
                        NavRoute.RecordDetail.createRoute(type.dataDirName, Uri.encode(name))
                    )
                },
                settingsViewModel = settingsViewModel
            )
        }

        composable(
            route = NavRoute.Settings.route,
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition },
            popEnterTransition = { defaultPopEnterTransition },
            popExitTransition = { defaultPopExitTransition }
        ) {
            SettingsScreen(
                settingsViewModel = settingsViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = NavRoute.PredictionDetail.route,
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition },
            popEnterTransition = { defaultPopEnterTransition },
            popExitTransition = { defaultPopExitTransition }
        ) {
            PredictionDetailScreen(
                settingsViewModel = settingsViewModel
            )
        }

        composable(
            route = NavRoute.HistoryList.route,
            arguments = listOf(navArgument("type") { type = NavType.StringType }),
            enterTransition = { defaultEnterTransition },
            exitTransition = {
                if (targetState.destination.route == NavRoute.RecordDetail.route) {
                    slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth / 4 }) +
                            fadeOut()
                } else {
                    defaultExitTransition
                }
            },
            popEnterTransition = {
                if (initialState.destination.route == NavRoute.RecordDetail.route) {
                    slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth / 4 }) +
                            fadeIn()
                } else {
                    defaultPopEnterTransition
                }
            },
            popExitTransition = { defaultPopExitTransition }
        ) { backStackEntry ->
            val typeArg =
                backStackEntry.arguments?.getString("type") ?: BatteryStatus.Charging.dataDirName
            val batteryStatus = if (typeArg == BatteryStatus.Discharging.dataDirName) {
                BatteryStatus.Discharging
            } else {
                BatteryStatus.Charging
            }
            HistoryListScreen(
                batteryStatus = batteryStatus,
                viewModel = historyViewModel,
                onNavigateToRecordDetail = { type, name ->
                    navController.navigate(
                        NavRoute.RecordDetail.createRoute(type.dataDirName, Uri.encode(name))
                    )
                },
                settingsViewModel = settingsViewModel
            )
        }

        composable(
            route = NavRoute.RecordDetail.route,
            arguments = listOf(
                navArgument("type") { type = NavType.StringType },
                navArgument("name") { type = NavType.StringType }
            ),
            enterTransition = { defaultEnterTransition },
            exitTransition = { defaultExitTransition },
            popEnterTransition = { defaultPopEnterTransition },
            popExitTransition = { defaultPopExitTransition }
        ) { backStackEntry ->
            val typeArg =
                backStackEntry.arguments?.getString("type") ?: BatteryStatus.Charging.dataDirName
            val nameArg = backStackEntry.arguments?.getString("name") ?: ""
            val batteryStatus = if (typeArg == BatteryStatus.Discharging.dataDirName) {
                BatteryStatus.Discharging
            } else {
                BatteryStatus.Charging
            }
            RecordDetailScreen(
                recordsFile = RecordsFile(batteryStatus, Uri.decode(nameArg)),
                viewModel = historyViewModel,
                settingsViewModel = settingsViewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
