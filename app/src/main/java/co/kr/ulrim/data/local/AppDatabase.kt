package co.kr.ulrim.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Sentence::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sentenceDao(): SentenceDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE sentences ADD COLUMN author TEXT")
        database.execSQL("ALTER TABLE sentences ADD COLUMN language TEXT NOT NULL DEFAULT 'en'")
        database.execSQL("ALTER TABLE sentences ADD COLUMN tags TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE sentences ADD COLUMN source TEXT NOT NULL DEFAULT 'LOCAL'")
    }
}
