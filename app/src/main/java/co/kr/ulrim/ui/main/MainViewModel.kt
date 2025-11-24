package co.kr.ulrim.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.local.Background
import co.kr.ulrim.data.local.Backgrounds
import co.kr.ulrim.data.local.Sentence
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SentenceRepository
) : ViewModel() {

    private val _currentSentence = MutableStateFlow<Sentence?>(null)
    val currentSentence: StateFlow<Sentence?> = _currentSentence.asStateFlow()

    private val _currentBackground = MutableStateFlow<Background>(Backgrounds.list.random())
    val currentBackground: StateFlow<Background> = _currentBackground.asStateFlow()

    init {
        loadRandomSentence()
    }

    fun loadRandomSentence() {
        viewModelScope.launch {
             val sentence = repository.getRandomSentence().first()
             _currentSentence.value = sentence
             _currentBackground.value = Backgrounds.list.random()
        }
    }
}
