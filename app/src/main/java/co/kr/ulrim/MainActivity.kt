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

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onNavigateToAdd = { navController.navigate("add") }
            )
        }
        composable("add") {
            AddSentenceScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}