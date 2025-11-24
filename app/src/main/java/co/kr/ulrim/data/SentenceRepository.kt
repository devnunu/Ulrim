package co.kr.ulrim.data

import co.kr.ulrim.data.local.Sentence
import co.kr.ulrim.data.local.SentenceDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SentenceRepository @Inject constructor(
    private val sentenceDao: SentenceDao
) {
    val allSentences: Flow<List<Sentence>> = sentenceDao.getAll()

    fun getRandomSentence(): Flow<Sentence?> = sentenceDao.getRandomSentence()

    suspend fun insert(sentence: Sentence) {
        sentenceDao.insert(sentence)
    }

    suspend fun delete(sentence: Sentence) {
        sentenceDao.delete(sentence)
    }
}
