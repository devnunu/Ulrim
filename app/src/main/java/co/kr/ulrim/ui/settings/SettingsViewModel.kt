package co.kr.ulrim.ui.settings

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SettingsRepository
import co.kr.ulrim.ui.widget.UlrimWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: SettingsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val userPreferences = repository.userPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun setFontSize(size: Int) {
        viewModelScope.launch {
            repository.setFontSize(size)
        }
    }

    fun setAnimationOn(isOn: Boolean) {
        viewModelScope.launch {
            repository.setAnimationOn(isOn)
        }
    }

    fun setBackgroundOn(isOn: Boolean) {
        viewModelScope.launch {
            repository.setBackgroundOn(isOn)
        }
    }

    fun setWidgetMode(mode: String) {
        viewModelScope.launch {
            repository.setWidgetMode(mode)
            // Trigger Widget Update
            val glanceId = GlanceAppWidgetManager(context).getGlanceIds(UlrimWidget::class.java).firstOrNull()
            if (glanceId != null) {
                UlrimWidget().update(context, glanceId)
            }
        }
    }

    fun setQuoteSource(source: String) {
        viewModelScope.launch {
            repository.setQuoteSource(source)
            // Trigger Widget Update since quote source affects widget too
            val glanceId = GlanceAppWidgetManager(context).getGlanceIds(UlrimWidget::class.java).firstOrNull()
            if (glanceId != null) {
                UlrimWidget().update(context, glanceId)
            }
        }
    }

    fun setWidgetStyle(style: String) {
        viewModelScope.launch {
            repository.setWidgetStyle(style)
            // Trigger Widget Update
            val glanceId = GlanceAppWidgetManager(context).getGlanceIds(UlrimWidget::class.java).firstOrNull()
            if (glanceId != null) {
                UlrimWidget().update(context, glanceId)
            }
        }
    }
}
