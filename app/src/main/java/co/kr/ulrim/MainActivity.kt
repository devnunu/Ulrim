package co.kr.ulrim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.kr.ulrim.ui.add.AddSentenceScreen
import co.kr.ulrim.ui.browse.BrowseQuotesScreen
import co.kr.ulrim.ui.detail.SentenceDetailScreen
import co.kr.ulrim.ui.list.SentenceListScreen
import co.kr.ulrim.ui.main.MainScreen
import co.kr.ulrim.ui.onboarding.OnboardingScreen
import co.kr.ulrim.ui.settings.SettingsScreen
import co.kr.ulrim.ui.splash.SplashScreen
import co.kr.ulrim.ui.theme.UlrimTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UlrimTheme {
                UlrimApp()
            }
        }
    }
}

@Composable
fun UlrimApp() {
    val navController = rememberNavController()
    val view = LocalView.current

    SideEffect {
        val window = (view.context as android.app.Activity).window
        window.statusBarColor = Color.Black.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate("onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("onboarding") {
            OnboardingScreen(
                onNavigateToAdd = {
                    navController.navigate("add") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                onNavigateToBrowse = {
                    navController.navigate("browse")
                },
                onSkip = {
                    navController.navigate("main") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }
        composable("browse") {
            BrowseQuotesScreen(
                onNavigateBack = { navController.popBackStack() },
                onComplete = {
                    navController.navigate("main") {
                        popUpTo("browse") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            MainScreen(
                onNavigateToAdd = { navController.navigate("add") },
                onNavigateToList = { navController.navigate("list") },
                onNavigateToSettings = { navController.navigate("settings") }
            )
        }
        composable("settings") {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("add") {
            AddSentenceScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("list") {
            SentenceListScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToDetail = { sentenceId ->
                    navController.navigate("detail/$sentenceId")
                }
            )
        }
        composable(
            route = "detail/{sentenceId}",
            arguments = listOf(androidx.navigation.navArgument("sentenceId") { type = androidx.navigation.NavType.LongType })
        ) {
            SentenceDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}