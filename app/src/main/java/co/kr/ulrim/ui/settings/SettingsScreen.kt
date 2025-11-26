package co.kr.ulrim.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.ulrim.ui.theme.UlrimColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val userPreferences by viewModel.userPreferences.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        if (userPreferences != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Font Size Section
                SettingsSectionTitle("Font Size")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.DarkGray.copy(alpha = 0.5f)),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FontSizeOption(
                        label = "Small",
                        isSelected = userPreferences!!.fontSize == 0,
                        onClick = { viewModel.setFontSize(0) }
                    )
                    FontSizeOption(
                        label = "Medium",
                        isSelected = userPreferences!!.fontSize == 1,
                        onClick = { viewModel.setFontSize(1) }
                    )
                    FontSizeOption(
                        label = "Large",
                        isSelected = userPreferences!!.fontSize == 2,
                        onClick = { viewModel.setFontSize(2) }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Animation Section
                SettingsToggleRow(
                    title = "Animation",
                    isChecked = userPreferences!!.isAnimationOn,
                    onCheckedChange = { viewModel.setAnimationOn(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Background Section
                SettingsToggleRow(
                    title = "Background Image",
                    isChecked = userPreferences!!.isBackgroundOn,
                    onCheckedChange = { viewModel.setBackgroundOn(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Quote Source Section (applies to both app and widget)
                SettingsSectionTitle("Quote Source")
                QuoteSourceSelector(
                    selectedSource = userPreferences!!.quoteSource,
                    onSourceSelected = { viewModel.setQuoteSource(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))



                Spacer(modifier = Modifier.height(24.dp))

                // Add Widget Section
                SettingsSectionTitle("Add Widget")
                Text(
                    text = "Add a widget to your home screen to see daily quotes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Default Widget Button
                    Button(
                        onClick = { 
                            viewModel.requestPinWidget(co.kr.ulrim.ui.widget.DefaultWidgetReceiver::class.java) 
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UlrimColors.SecondaryBackground,
                            contentColor = UlrimColors.TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Default Style")
                    }

                    // Simple Widget Button
                    Button(
                        onClick = { 
                            viewModel.requestPinWidget(co.kr.ulrim.ui.widget.SimpleWidgetReceiver::class.java) 
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UlrimColors.SecondaryBackground,
                            contentColor = UlrimColors.TextPrimary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Simple Style")
                    }
                }
            }
        }
    }
}


@Composable
fun QuoteSourceSelector(
    selectedSource: String,
    onSourceSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.DarkGray.copy(alpha = 0.5f))
    ) {
        QuoteSourceOption(
            label = "My Quotes Only",
            isSelected = selectedSource == "local_only",
            onClick = { onSourceSelected("local_only") }
        )
        QuoteSourceOption(
            label = "Default Quotes Only",
            isSelected = selectedSource == "remote_only",
            onClick = { onSourceSelected("remote_only") }
        )
        QuoteSourceOption(
            label = "Both (Random)",
            isSelected = selectedSource == "both",
            onClick = { onSourceSelected("both") }
        )
    }
}

@Composable
fun QuoteSourceOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun WidgetModeSelector(
    selectedMode: String,
    onModeSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.DarkGray.copy(alpha = 0.5f)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WidgetModeOption(
            label = "Daily Quote",
            isSelected = selectedMode == "daily",
            onClick = { onModeSelected("daily") }
        )
        WidgetModeOption(
            label = "Random Quote",
            isSelected = selectedMode == "random",
            onClick = { onModeSelected("random") }
        )
    }
}

@Composable
fun WidgetModeOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp)
            .background(if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = Color.Gray,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun FontSizeOption(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent, RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun SettingsToggleRow(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color.DarkGray,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.Black
            )
        )
    }
}
