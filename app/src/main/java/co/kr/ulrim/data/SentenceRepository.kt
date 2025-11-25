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

    fun getRandomSentenceBySource(sourceFilter: String): Flow<Sentence?> {
        return when (sourceFilter) {
            "local_only" -> sentenceDao.getRandomSentenceBySource("LOCAL")
            "remote_only" -> sentenceDao.getRandomSentenceBySource("REMOTE")
            "both" -> sentenceDao.getRandomSentence()
            else -> sentenceDao.getRandomSentence()
        }
    }

    suspend fun insert(sentence: Sentence) {
        sentenceDao.insert(sentence)
    }

    suspend fun insertAll(sentences: List<Sentence>) {
        sentenceDao.insertAll(sentences)
    }

    suspend fun update(sentence: Sentence) {
        sentenceDao.update(sentence)
    }

    suspend fun delete(sentence: Sentence) {
        sentenceDao.delete(sentence)
    }

    suspend fun deleteBySource(source: String) {
        sentenceDao.deleteBySource(source)
    }

    suspend fun getById(id: Long): Sentence? {
        return sentenceDao.getById(id)
    }
}
