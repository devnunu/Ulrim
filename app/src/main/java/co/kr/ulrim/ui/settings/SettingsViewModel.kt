package co.kr.ulrim.ui.settings

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SettingsRepository

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

    fun setQuoteSource(source: String) {
        viewModelScope.launch {
            repository.setQuoteSource(source)
            // Trigger Widget Update for both widgets
            val manager = GlanceAppWidgetManager(context)
            
            // Update DefaultWidget
            val defaultWidgetIds = manager.getGlanceIds(co.kr.ulrim.ui.widget.DefaultWidget::class.java)
            defaultWidgetIds.forEach { glanceId ->
                co.kr.ulrim.ui.widget.DefaultWidget().update(context, glanceId)
            }

            // Update SimpleWidget
            val simpleWidgetIds = manager.getGlanceIds(co.kr.ulrim.ui.widget.SimpleWidget::class.java)
            simpleWidgetIds.forEach { glanceId ->
                co.kr.ulrim.ui.widget.SimpleWidget().update(context, glanceId)
            }
        }
    }

    fun requestPinWidget(widgetClass: Class<*>) {
        val appWidgetManager = android.appwidget.AppWidgetManager.getInstance(context)
        val myProvider = android.content.ComponentName(context, widgetClass)

        if (appWidgetManager.isRequestPinAppWidgetSupported) {
            appWidgetManager.requestPinAppWidget(myProvider, null, null)
        }
    }
}
