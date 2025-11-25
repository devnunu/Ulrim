package co.kr.ulrim.ui.onboarding

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.ulrim.ui.theme.UlrimColors
import co.kr.ulrim.ui.theme.UlrimTypography

@Composable
fun OnboardingScreen(
    onNavigateToAdd: () -> Unit,
    onNavigateToBrowse: () -> Unit,
    onSkip: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
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
