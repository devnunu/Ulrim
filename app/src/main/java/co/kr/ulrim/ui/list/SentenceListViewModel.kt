package co.kr.ulrim.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.local.Sentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentenceListViewModel @Inject constructor(
    private val repository: SentenceRepository
) : ViewModel() {

    val sentences: StateFlow<List<Sentence>> = repository.allSentences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteSentence(sentence: Sentence) {
        viewModelScope.launch {
            repository.delete(sentence)
        }
    }
}
