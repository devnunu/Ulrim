package co.kr.ulrim.data.local

import androidx.annotation.DrawableRes
import co.kr.ulrim.R

data class Background(
    val id: String,
    @DrawableRes val resourceId: Int
)

object Backgrounds {
    val list = listOf(
        Background("1", R.drawable.img_galaxy_1),
        Background("2", R.drawable.img_sky_1),
        Background("3", R.drawable.img_nature_1),
        Background("4", R.drawable.img_galaxy_2)
    )
}
