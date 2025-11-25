package co.kr.ulrim.data.model

data class QuotesData(
    val version: Int,
    val quotes: List<QuoteItem>
)

data class QuoteItem(
    val id: String,
    val text: String,
    val author: String,
    val language: String,
    val tags: List<String>
)
