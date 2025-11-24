package co.kr.ulrim.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SentenceDao {
    @Query("SELECT * FROM sentences ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Sentence>>

    @Query("SELECT * FROM sentences ORDER BY RANDOM() LIMIT 1")
    fun getRandomSentence(): Flow<Sentence?>

    @Insert
    suspend fun insert(sentence: Sentence)

    @androidx.room.Update
    suspend fun update(sentence: Sentence)

    @Delete
    suspend fun delete(sentence: Sentence)

    @Query("SELECT * FROM sentences WHERE id = :id")
    suspend fun getById(id: Long): Sentence?
}
