package co.kr.ulrim.ui.browse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.local.Sentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BrowseQuotesViewModel @Inject constructor(
    private val repository: SentenceRepository
) : ViewModel() {

    val remoteQuotes: StateFlow<List<Sentence>> = repository.allSentences
        .map { sentences -> sentences.filter { it.source == "REMOTE" } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _selectedQuoteIds = MutableStateFlow<Set<Long>>(emptySet())
    val selectedQuoteIds: StateFlow<Set<Long>> = _selectedQuoteIds.asStateFlow()

    fun toggleQuoteSelection(quoteId: Long) {
        _selectedQuoteIds.value = if (_selectedQuoteIds.value.contains(quoteId)) {
            _selectedQuoteIds.value - quoteId
        } else {
            _selectedQuoteIds.value + quoteId
        }
    }

    fun saveSelectedQuotes(onComplete: () -> Unit) {
        viewModelScope.launch {
            val selectedQuotes = remoteQuotes.value.filter { 
                _selectedQuoteIds.value.contains(it.id) 
            }
            
            val localQuotes = selectedQuotes.map { quote ->
                quote.copy(
                    id = 0, // Auto-generate new ID
                    source = "LOCAL",
                    createdAt = System.currentTimeMillis()
                )
            }
            
            repository.insertAll(localQuotes)
            onComplete()
        }
    }
}
