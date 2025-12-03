package co.kr.ulrim.ui.main

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToList: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val currentBackground by viewModel.currentBackground.collectAsState()
    val currentSentence by viewModel.currentSentence.collectAsState()
    val userPreferences by viewModel.userPreferences.collectAsState()

    // Back press handling
    var backPressedTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        val currentTime = System.currentTimeMillis()
        if (currentTime - backPressedTime < 2000) {
            // Second back press within 2 seconds - exit app
            (context as? Activity)?.finish()
        } else {
            // First back press - show toast
            backPressedTime = currentTime
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
        }
    }

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
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(background.imageUrl)
                            .crossfade(true)
                            .build(),
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

        // Share Button
        var showShareDialog by remember { mutableStateOf(false) }
        IconButton(
            onClick = { showShareDialog = true },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 72.dp, top = 48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
        }

        if (showShareDialog) {
            ShareActionSheet(
                onDismiss = { showShareDialog = false },
                onShareText = {
                    viewModel.shareAsText()
                    showShareDialog = false
                },
                onShareImage = {
                    viewModel.shareAsImage()
                    showShareDialog = false
                }
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

        // Add Button (Subtle Floating Action Button)
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable { onNavigateToAdd() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Sentence",
                tint = Color.White.copy(alpha = 0.9f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
    }
}

@Composable
fun ShareActionSheet(
    onDismiss: () -> Unit,
    onShareText: () -> Unit,
    onShareImage: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Share Quote", color = Color.White) },
        text = {
            Column {
                androidx.compose.material3.TextButton(
                    onClick = onShareText,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Share as Text", color = Color.White)
                }
                androidx.compose.material3.TextButton(
                    onClick = onShareImage,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Share as Image", color = Color.White)
                }
            }
        },
        confirmButton = {
            androidx.compose.material3.TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.White)
            }
        },
        containerColor = Color.DarkGray
    )
}
