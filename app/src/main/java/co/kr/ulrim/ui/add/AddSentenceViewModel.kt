package co.kr.ulrim.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.local.Sentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddSentenceViewModel @Inject constructor(
    private val repository: SentenceRepository
) : ViewModel() {

    fun saveSentence(content: String) {
        if (content.isBlank()) return
        viewModelScope.launch {
            repository.insert(Sentence(content = content))
        }
    }
}
