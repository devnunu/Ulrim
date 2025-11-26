package co.kr.ulrim.ui.onboarding

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.ulrim.ui.theme.UlrimColors
import co.kr.ulrim.ui.theme.UlrimTypography
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToBrowse: () -> Unit,
    onSkip: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    // Infinite ripple animation
    val rippleAlpha = remember { Animatable(0f) }
    val rippleScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Infinite ripple animation - run both in parallel
        launch {
            rippleAlpha.animateTo(
                targetValue = 0.6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
        launch {
            rippleScale.animateTo(
                targetValue = 1.3f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UlrimColors.PrimaryBackground),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ripple Animation
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val centerX = size.width / 2
                    val centerY = size.height / 2
                    val baseRadius = 20f

                    // Center dot
                    drawCircle(
                        color = UlrimColors.Accent.copy(alpha = rippleAlpha.value * 0.8f),
                        radius = 6f,
                        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
                    )

                    // Ripple rings
                    for (i in 1..3) {
                        val radius = baseRadius * i * rippleScale.value
                        val alpha = rippleAlpha.value * (1f - (i * 0.2f))
                        drawCircle(
                            color = UlrimColors.Accent.copy(alpha = alpha),
                            radius = radius,
                            center = androidx.compose.ui.geometry.Offset(centerX, centerY),
                            style = Stroke(width = 2f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title
            Text(
                text = "Welcome to Ulrim",
                style = UlrimTypography.Headline.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light
                ),
                color = UlrimColors.TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle
            Text(
                text = "Find your principle.\nReset your mind.",
                style = UlrimTypography.Body.copy(
                    fontSize = 18.sp
                ),
                color = UlrimColors.TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            // Add First Quote Button
            Button(
                onClick = {
                    viewModel.completeOnboarding()
                    onNavigateToAdd()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UlrimColors.Accent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Add My First Quote",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Browse Default Quotes Button
            OutlinedButton(
                onClick = {
                    viewModel.completeOnboarding()
                    onNavigateToBrowse()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = UlrimColors.TextPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Browse Default Quotes",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Skip Button
            Box(
                modifier = Modifier
                    .clickable {
                        viewModel.completeOnboarding()
                        onSkip()
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = "Skip for now",
                    style = UlrimTypography.Caption,
                    color = UlrimColors.TextSecondary.copy(alpha = 0.7f)
                )
            }
        }
    }
}
