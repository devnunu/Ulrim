package co.kr.ulrim.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import co.kr.ulrim.MainActivity
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.SettingsRepository
import co.kr.ulrim.domain.DailyQuoteManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class SimpleWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface SimpleWidgetEntryPoint {
        fun dailyQuoteManager(): DailyQuoteManager
        fun sentenceRepository(): SentenceRepository
        fun settingsRepository(): SettingsRepository
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val entryPoint = EntryPointAccessors.fromApplication(
            appContext,
            SimpleWidgetEntryPoint::class.java
        )

        val dailyQuoteManager = entryPoint.dailyQuoteManager()
        val settingsRepository = entryPoint.settingsRepository()

        val quoteSource = runBlocking {
            settingsRepository.userPreferences.first().quoteSource
        }

        // Always use daily quote for simple widget
        val quote = runBlocking {
            dailyQuoteManager.getOrUpdateTodayQuote(quoteSource)
        }

        provideContent {
            SimpleWidgetContent(quote?.content ?: "Tap to find your principle.")
        }
    }

    @Composable
    private fun SimpleWidgetContent(quoteText: String) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color.Transparent))
                .clickable(actionStartActivity<MainActivity>())
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = quoteText,
                style = TextStyle(
                    color = ColorProvider(Color.White),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                ),
                maxLines = 6
            )
        }
    }
}
