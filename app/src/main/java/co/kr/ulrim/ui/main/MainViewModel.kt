package co.kr.ulrim.ui.main

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.SettingsRepository
import co.kr.ulrim.data.local.Background
import co.kr.ulrim.data.local.Backgrounds
import co.kr.ulrim.data.local.Sentence
import co.kr.ulrim.domain.DailyQuoteManager

import co.kr.ulrim.util.ShareCardGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
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

    private val _currentBackground = MutableStateFlow<Background>(Background(Backgrounds.getRandomBackgroundUrl()))
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

        // Automatically load a quote when sentences are added
        viewModelScope.launch {
            repository.allSentences.collect { sentences ->
                // If we have no current sentence but sentences exist, load one
                if (_currentSentence.value == null && sentences.isNotEmpty()) {
                    loadRandomSentence()
                }
            }
        }
    }

    fun loadRandomSentence() {
        viewModelScope.launch {
             val sourceFilter = userPreferences.value?.quoteSource ?: "both"
             val sentence = repository.getRandomSentenceBySource(sourceFilter).first()
             _currentSentence.value = sentence
             _currentBackground.value = Background(Backgrounds.getRandomBackgroundUrl())
        }
    }

    private fun checkAndUpdateTodayQuote() {
        viewModelScope.launch {
            dailyQuoteManager.getOrUpdateTodayQuote()
            // Update Widgets
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

    fun shareAsText() {
        val sentence = _currentSentence.value?.content ?: return
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, sentence)
        }
        val chooserIntent = Intent.createChooser(shareIntent, "Share Quote")
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }

    fun shareAsImage() {
        viewModelScope.launch {
            val sentence = _currentSentence.value?.content ?: return@launch
            val backgroundUrl = _currentBackground.value.imageUrl

            // Load image bitmap using Coil
            val loader = coil.ImageLoader(context)
            val request = coil.request.ImageRequest.Builder(context)
                .data(backgroundUrl)
                .allowHardware(false) // Disable hardware bitmaps for canvas drawing
                .build()
            
            val result = (loader.execute(request) as? coil.request.SuccessResult)?.drawable
            val backgroundBitmap = (result as? android.graphics.drawable.BitmapDrawable)?.bitmap

            if (backgroundBitmap != null) {
                // Generate card bitmap with loaded background
                val bitmap = ShareCardGenerator.generateQuoteCard(context, sentence, backgroundBitmap)

                // Save to cache and share
                try {
                    val cachePath = File(context.cacheDir, "images")
                    cachePath.mkdirs()
                    val file = File(cachePath, "quote_card.png")
                    val stream = FileOutputStream(file)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                    stream.close()

                    val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "image/png"
                        putExtra(Intent.EXTRA_STREAM, contentUri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    val chooserIntent = Intent.createChooser(shareIntent, "Share Quote Card")
                    chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(chooserIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
