package co.kr.ulrim.ui.browse

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
import androidx.compose.material.icons.filled.FavoriteBorder
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
    viewModel: BrowseQuotesViewModel = hiltViewModel()
) {
    val remoteQuotes by viewModel.remoteQuotes.collectAsState()
    val savedQuoteIds by viewModel.savedQuoteIds.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Default Quotes", color = Color.White) },
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
                    QuoteCard(
                        quote = quote,
                        isSaved = savedQuoteIds.contains(quote.id),
                        onSaveClick = { viewModel.saveQuoteToLocal(quote) }
                    )
                }
            }
        }
    }
}

@Composable
fun QuoteCard(
    quote: Sentence,
    isSaved: Boolean,
    onSaveClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = UlrimColors.SecondaryBackground
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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

            Spacer(modifier = Modifier.height(12.dp))

            // Save Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isSaved) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Saved",
                            tint = UlrimColors.Accent
                        )
                        Text(
                            text = "Saved",
                            style = MaterialTheme.typography.bodySmall,
                            color = UlrimColors.Accent,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                } else {
                    IconButton(onClick = onSaveClick) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "Save to My Quotes",
                            tint = UlrimColors.Accent
                        )
                    }
                }
            }
        }
    }
}
