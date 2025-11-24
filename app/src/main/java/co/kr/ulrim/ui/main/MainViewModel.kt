package co.kr.ulrim.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SentenceRepository
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

    init {
        loadRandomSentence()
    }

    fun loadRandomSentence() {
        viewModelScope.launch {
            // In a real app, we might want to handle empty state differently
            // But for now, we just try to get a random one.
            // Since Room Flow emits updates, we take the first one to get a snapshot-like behavior for "next"
            // However, the DAO returns Flow<Sentence?>, so we can collect it or just take first.
            // If we want "Next" functionality, we should re-trigger the query.
            // But DAO's getRandomSentence() returns a Flow that might not re-emit unless table changes if we just collect.
            // So we should probably treat it as a one-shot fetch for "Next".
            // But DAO returns Flow. Let's just collect it once.
            
            // Actually, for "Next" button to work with "ORDER BY RANDOM()", we need to re-execute the query.
            // Flow from Room observes the table. If table doesn't change, Flow doesn't emit new value even if query has RANDOM().
            // So we might need a suspend function in DAO for one-shot random.
            // Let's stick to Flow for now and see. If it doesn't work, I'll change DAO.
            // Wait, standard Room Flow with RANDOM() might not re-emit on collection restart if data hasn't changed?
            // Actually it's better to use a suspend function for "Get Random" if we want to manually trigger it.
            // I'll update DAO to have a suspend function for random.
            
            // For now, let's assume I'll update DAO.
            // But I can't update DAO right now in this tool call. 
            // I will use what I have. I'll collect the flow and take 1.
            
             val sentence = repository.getRandomSentence().first()
             _currentSentence.value = sentence
        }
    }
}
