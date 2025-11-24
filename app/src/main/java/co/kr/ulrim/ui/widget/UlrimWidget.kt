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
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import co.kr.ulrim.MainActivity
import co.kr.ulrim.domain.DailyQuoteManager
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking

class UlrimWidget : GlanceAppWidget() {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface UlrimWidgetEntryPoint {
        fun dailyQuoteManager(): DailyQuoteManager
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val appContext = context.applicationContext
        val dailyQuoteManager = EntryPointAccessors.fromApplication(
            appContext,
            UlrimWidgetEntryPoint::class.java
        ).dailyQuoteManager()

        // Fetch quote synchronously for widget provider
        val quote = runBlocking { dailyQuoteManager.getOrUpdateTodayQuote() }

        provideContent {
            UlrimWidgetContent(quote?.content ?: "Tap to find your principle.")
        }
    }

    @Composable
    private fun UlrimWidgetContent(quoteText: String) {
        Box(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(Color(0xFF121212))) // Dark background
                .clickable(actionStartActivity<MainActivity>())
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Ulrim",
                    style = TextStyle(
                        color = ColorProvider(Color.White.copy(alpha = 0.5f)),
                        fontSize = 12.sp
                    )
                )
                Spacer(modifier = GlanceModifier.height(12.dp))
                Text(
                    text = quoteText,
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 4
                )
                Spacer(modifier = GlanceModifier.height(12.dp))
                Text(
                    text = "Today's Quote",
                    style = TextStyle(
                        color = ColorProvider(Color.White.copy(alpha = 0.5f)),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }
}
