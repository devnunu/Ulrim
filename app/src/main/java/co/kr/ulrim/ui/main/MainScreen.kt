package co.kr.ulrim.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToList: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val currentBackground by viewModel.currentBackground.collectAsState()
    val currentSentence by viewModel.currentSentence.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()

    // Default values if preferences are not yet loaded
    val fontSizeScale = when (userPreferences?.fontSize) {
        0 -> 0.8f // Small
        2 -> 1.3f // Large
        else -> 1.0f // Medium
    }
    val isAnimationOn = userPreferences?.isAnimationOn ?: true
    val isBackgroundOn = userPreferences?.isBackgroundOn ?: true

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .clickable {
                    viewModel.loadRandomSentence()
                }
        ) {
            // Background Image
            AnimatedContent(
                targetState = currentBackground,
                transitionSpec = {
                    if (isAnimationOn) {
                        (fadeIn(animationSpec = tween(1000))).togetherWith(fadeOut(animationSpec = tween(1000)))
                    } else {
                        EnterTransition.None togetherWith ExitTransition.None
                    }
                },
                label = "BackgroundAnimation"
            ) { background ->
                if (isBackgroundOn) {
                    Image(
                        painter = painterResource(id = background.resourceId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF121212)) // Dark solid color
                    )
                }
            }

            // Overlay (Only if background is on)
            if (isBackgroundOn) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.4f))
                )
            }

            // Sentence Text
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = currentSentence,
                    transitionSpec = {
                        if (isAnimationOn) {
                            (fadeIn(animationSpec = tween(800)) + slideInVertically { it / 2 })
                                .togetherWith(fadeOut(animationSpec = tween(800)) + slideOutVertically { -it / 2 })
                        } else {
                            EnterTransition.None togetherWith ExitTransition.None
                        }
                    },
                    label = "SentenceAnimation"
                ) { sentence ->
                    if (sentence != null) {
                        Text(
                            text = sentence.content,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize * fontSizeScale
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Text(
                            text = "Tap to find your principle.\nOr add a new one.",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = MaterialTheme.typography.titleLarge.fontSize * fontSizeScale
                            ),
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
            }
        }


        // Settings Button
        IconButton(
            onClick = onNavigateToSettings,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
                .padding(top = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
        }

        // List Button
        IconButton(
            onClick = onNavigateToList,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(24.dp)
                .padding(top = 24.dp) // Extra padding for status bar
        ) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "List",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
        }

        // Add Button (Subtle)
        IconButton(
            onClick = onNavigateToAdd,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Sentence",
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(24.dp)
            )
        }
    }
    }
}
