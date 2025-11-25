package co.kr.ulrim.domain

import android.content.Context
import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.SettingsRepository
import co.kr.ulrim.data.local.Sentence
import co.kr.ulrim.data.model.QuotesData
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuotesSyncManager @Inject constructor(
    private val sentenceRepository: SentenceRepository,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context
) {

    suspend fun syncDefaultQuotes() {
        val storedVersion = settingsRepository.getDefaultQuotesVersion()
        val quotesData = loadQuotesFromJson()

        if (quotesData.version > storedVersion) {
            // Delete all REMOTE quotes
            sentenceRepository.deleteBySource("REMOTE")

            // Insert new REMOTE quotes
            val sentences = quotesData.quotes.map { quote ->
                Sentence(
                    content = quote.text,
                    author = quote.author,
                    language = quote.language,
                    tags = quote.tags.joinToString(","),
                    source = "REMOTE"
                )
            }
            sentenceRepository.insertAll(sentences)

            // Update stored version
            settingsRepository.setDefaultQuotesVersion(quotesData.version)
        }
    }

    private fun loadQuotesFromJson(): QuotesData {
        val inputStream = context.resources.openRawResource(
            context.resources.getIdentifier("ulrim_quotes", "raw", context.packageName)
        )
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return Gson().fromJson(jsonString, QuotesData::class.java)
    }
}
