package co.kr.ulrim.ui.browse

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import co.kr.ulrim.data.local.Sentence
import co.kr.ulrim.ui.theme.UlrimColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseQuotesScreen(
    onNavigateBack: () -> Unit,
    onComplete: () -> Unit = {},
    viewModel: BrowseQuotesViewModel = hiltViewModel()
) {
    val remoteQuotes by viewModel.remoteQuotes.collectAsState()
    val selectedQuoteIds by viewModel.selectedQuoteIds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Default Quotes", color = Color.White)
                        if (selectedQuoteIds.isNotEmpty()) {
                            Text(
                                "${selectedQuoteIds.size} selected",
                                style = MaterialTheme.typography.bodySmall,
                                color = UlrimColors.Accent
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = UlrimColors.PrimaryBackground
                )
            )
        },
        bottomBar = {
            if (selectedQuoteIds.isNotEmpty()) {
                androidx.compose.material3.Button(
                    onClick = {
                        viewModel.saveSelectedQuotes {
                            onComplete()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = UlrimColors.Accent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Save ${selectedQuoteIds.size} Quote${if (selectedQuoteIds.size > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                        )
                    )
                }
            }
        },
        containerColor = UlrimColors.PrimaryBackground
    ) { padding ->
        if (remoteQuotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No default quotes available.\nPlease check your connection.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = UlrimColors.TextSecondary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(remoteQuotes) { quote ->
                    SelectableQuoteCard(
                        quote = quote,
                        isSelected = selectedQuoteIds.contains(quote.id),
                        onToggleSelection = { viewModel.toggleQuoteSelection(quote.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectableQuoteCard(
    quote: Sentence,
    isSelected: Boolean,
    onToggleSelection: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleSelection() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) 
                UlrimColors.Accent.copy(alpha = 0.2f) 
            else 
                UlrimColors.SecondaryBackground
        ),
        shape = RoundedCornerShape(12.dp),
        border = if (isSelected) 
            BorderStroke(2.dp, UlrimColors.Accent) 
        else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Quote Text
                Text(
                    text = quote.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = UlrimColors.TextPrimary,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )

                if (quote.author != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "— ${quote.author}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontStyle = FontStyle.Italic
                        ),
                        color = UlrimColors.TextSecondary
                    )
                }
            }

            // Selection indicator
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = UlrimColors.Accent,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}
