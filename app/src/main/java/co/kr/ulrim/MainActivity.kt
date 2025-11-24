package co.kr.ulrim

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
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
                    navController.navigate("main") {
                        popUpTo("splash") { inclusive = true }
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