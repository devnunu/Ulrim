package co.kr.ulrim.di

import android.content.Context
import androidx.room.Room
import co.kr.ulrim.data.local.AppDatabase
import co.kr.ulrim.data.local.SentenceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ulrim_database"
        ).addMigrations(co.kr.ulrim.data.local.MIGRATION_1_2).build()
    }

    @Provides
    fun provideSentenceDao(database: AppDatabase): SentenceDao {
        return database.sentenceDao()
    }
}
