package co.kr.ulrim.ui.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.local.Sentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SentenceDetailViewModel @Inject constructor(
    private val repository: SentenceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sentenceId: Long = checkNotNull(savedStateHandle["sentenceId"])

    private val _sentence = MutableStateFlow<Sentence?>(null)
    val sentence: StateFlow<Sentence?> = _sentence.asStateFlow()

    init {
        loadSentence()
    }

    private fun loadSentence() {
        viewModelScope.launch {
            _sentence.value = repository.getById(sentenceId)
        }
    }

    fun updateSentence(content: String) {
        val current = _sentence.value ?: return
        if (content.isBlank()) return

        viewModelScope.launch {
            repository.update(current.copy(content = content))
        }
    }

    fun deleteSentence(onDeleted: () -> Unit) {
        val current = _sentence.value ?: return
        viewModelScope.launch {
            repository.delete(current)
            onDeleted()
        }
    }
}
