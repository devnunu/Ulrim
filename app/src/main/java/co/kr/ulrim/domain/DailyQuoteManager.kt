package co.kr.ulrim.domain

import co.kr.ulrim.data.SentenceRepository
import co.kr.ulrim.data.SettingsRepository
import co.kr.ulrim.data.local.Sentence
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DailyQuoteManager @Inject constructor(
    private val sentenceRepository: SentenceRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend fun getOrUpdateTodayQuote(sourceFilter: String = "both"): Sentence? {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val (savedDate, savedQuoteId) = settingsRepository.todayQuoteData.first()

        if (savedDate == todayDate && savedQuoteId != null) {
            val savedSentence = sentenceRepository.getById(savedQuoteId)
            if (savedSentence != null) {
                return savedSentence
            }
        }

        // Need new quote
        val randomSentence = sentenceRepository.getRandomSentenceBySource(sourceFilter).firstOrNull() ?: return null
        
        settingsRepository.setTodayQuote(todayDate, randomSentence.id)
        return randomSentence
    }
}
