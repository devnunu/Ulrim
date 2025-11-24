package co.kr.ulrim.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MainScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToList: () -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val currentBackground by viewModel.currentBackground.collectAsState()
    val currentSentence by viewModel.currentSentence.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { viewModel.loadRandomSentence() }
    ) {
        // Background Image (Animated)
        AnimatedContent(
            targetState = currentBackground,
            transitionSpec = { fadeIn() with fadeOut() },
            label = "BackgroundTransition",
            modifier = Modifier.fillMaxSize()
        ) { background ->
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = background.resourceId),
                contentDescription = null,
                contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Content
        AnimatedContent(
            targetState = currentSentence,
            transitionSpec = { fadeIn() with fadeOut() },
            label = "SentenceTransition",
            modifier = Modifier.align(Alignment.Center)
        ) { sentence ->
            if (sentence != null) {
                Text(
                    text = sentence.content,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            } else {
                Text(
                    text = "Tap to find your principle.\nOr add a new one.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(32.dp)
                )
            }
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
