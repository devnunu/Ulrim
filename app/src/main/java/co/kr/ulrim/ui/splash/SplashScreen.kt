package co.kr.ulrim.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import co.kr.ulrim.ui.theme.UlrimColors
import co.kr.ulrim.ui.theme.UlrimTypography
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateToMain: () -> Unit
) {
    val rippleAlpha = remember { Animatable(0f) }
    val rippleScale = remember { Animatable(1f) }
    val textAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Ripple animation
        rippleAlpha.animateTo(
            targetValue = 0.4f,
            animationSpec = tween(durationMillis = 400)
        )
        rippleScale.animateTo(
            targetValue = 1.05f,
            animationSpec = tween(durationMillis = 400)
        )
        
        // Text animation (delayed)
        delay(100)
        textAlpha.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(durationMillis = 300)
        )
        
        // Wait and navigate
        delay(100)
        onNavigateToMain()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UlrimColors.PrimaryBackground),
        contentAlignment = Alignment.Center
    ) {
        // Ripple animation
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerX = size.width / 2
            val centerY = size.height / 2
            val baseRadius = 40f

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

        // Ulrim text
        Text(
            text = "Ulrim",
            style = UlrimTypography.Caption.copy(
                color = UlrimColors.TextPrimary.copy(alpha = textAlpha.value)
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-80).dp)
        )
    }
}
