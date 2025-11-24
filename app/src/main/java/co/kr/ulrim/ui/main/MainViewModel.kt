package co.kr.ulrim.ui.main

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.SettingsRepository
import co.kr.ulrim.data.local.Background
import co.kr.ulrim.data.local.Backgrounds
import co.kr.ulrim.data.local.Sentence
import co.kr.ulrim.domain.DailyQuoteManager
import co.kr.ulrim.ui.widget.UlrimWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: SentenceRepository,
    private val settingsRepository: SettingsRepository,
    private val dailyQuoteManager: DailyQuoteManager,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _currentSentence = MutableStateFlow<Sentence?>(null)
    val currentSentence: StateFlow<Sentence?> = _currentSentence.asStateFlow()

    private val _currentBackground = MutableStateFlow<Background>(Backgrounds.list.random())
    val currentBackground: StateFlow<Background> = _currentBackground.asStateFlow()

    val userPreferences = settingsRepository.userPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    init {
        loadRandomSentence()
        checkAndUpdateTodayQuote()
    }

    fun loadRandomSentence() {
        viewModelScope.launch {
             val sentence = repository.getRandomSentence().first()
             _currentSentence.value = sentence
             _currentBackground.value = Backgrounds.list.random()
        }
    }

    private fun checkAndUpdateTodayQuote() {
        viewModelScope.launch {
            dailyQuoteManager.getOrUpdateTodayQuote()
            // Update Widget
            val glanceId = GlanceAppWidgetManager(context).getGlanceIds(UlrimWidget::class.java).firstOrNull()
            if (glanceId != null) {
                UlrimWidget().update(context, glanceId)
            }
        }
    }
}
