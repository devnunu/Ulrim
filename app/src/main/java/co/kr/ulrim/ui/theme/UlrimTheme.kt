package co.kr.ulrim.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object UlrimColors {
    val PrimaryBackground = Color(0xFF0A0F14)
    val SecondaryBackground = Color(0xFF0E1319)
    val TertiaryBackground = Color(0xFF11161C)
    val TextPrimary = Color(0xFFFFFFFF).copy(alpha = 0.9f)
    val TextSecondary = Color(0xFFC3CCD9).copy(alpha = 0.6f)
    val Accent = Color(0xFF9FB3FF).copy(alpha = 0.7f)
    val Divider = Color(0xFFFFFFFF).copy(alpha = 0.12f)
}

object UlrimTypography {
    val Headline = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 26.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 38.sp,
        color = UlrimColors.TextPrimary
    )
    
    val Body = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 24.sp,
        color = UlrimColors.TextPrimary
    )
    
    val Caption = TextStyle(
        fontFamily = FontFamily.Serif,
        fontSize = 13.sp,
        fontWeight = FontWeight.Light,
        letterSpacing = 2.sp,
        color = UlrimColors.TextSecondary
    )
}

object UlrimSpacing {
    val HorizontalPadding = 24.dp
    val VerticalSpacing = 48.dp
    val Small = 8.dp
    val Medium = 16.dp
    val Large = 32.dp
}

object UlrimAnimations {
    const val FadeDuration = 200
    const val SlideDuration = 150
    const val ScaleDuration = 100
}
