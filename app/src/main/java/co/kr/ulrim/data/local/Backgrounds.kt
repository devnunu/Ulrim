package co.kr.ulrim.data.local

data class Background(
    val imageUrl: String
)

object Backgrounds {
    fun getRandomBackgroundUrl(): String {
        return "https://picsum.photos/1080/1920?random=${System.currentTimeMillis()}"
    }
}
