package co.kr.ulrim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.kr.ulrim.ui.add.AddSentenceScreen
import co.kr.ulrim.ui.main.MainScreen
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

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            co.kr.ulrim.ui.splash.SplashScreen(
                onNavigateToMain = {
                    // Navigation will be handled by the composable itself
                    // For now, always go to onboarding - we'll fix this
                    navController.navigate("check_onboarding") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
            )
        }
        composable("check_onboarding") {
            // This is a helper composable to check onboarding status
            val viewModel: co.kr.ulrim.ui.splash.SplashViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val shouldShowOnboarding by viewModel.shouldShowOnboarding.collectAsState()
            
            androidx.compose.runtime.LaunchedEffect(shouldShowOnboarding) {
                if (shouldShowOnboarding) {
                    navController.navigate("onboarding") {
                        popUpTo("check_onboarding") { inclusive = true }
                    }
                } else {
                    navController.navigate("main") {
                        popUpTo("check_onboarding") { inclusive = true }
                    }
                }
            }
        }
        composable("onboarding") {
            co.kr.ulrim.ui.onboarding.OnboardingScreen(
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
            co.kr.ulrim.ui.browse.BrowseQuotesScreen(
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
            co.kr.ulrim.ui.settings.SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("add") {
            AddSentenceScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("list") {
            co.kr.ulrim.ui.list.SentenceListScreen(
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
            co.kr.ulrim.ui.detail.SentenceDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}