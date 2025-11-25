package co.kr.ulrim.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sentences")
data class Sentence(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val author: String? = null,
    val language: String = "en",
    val tags: String = "", // Comma-separated tags
    val source: String = "LOCAL", // "LOCAL" or "REMOTE"
    val createdAt: Long = System.currentTimeMillis(),
    val backgroundId: Int? = null
)
