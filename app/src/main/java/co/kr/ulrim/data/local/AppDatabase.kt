package co.kr.ulrim.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import co.kr.ulrim.data.local.Sentence
import co.kr.ulrim.data.local.SentenceDao

@Database(entities = [Sentence::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sentenceDao(): SentenceDao
}
