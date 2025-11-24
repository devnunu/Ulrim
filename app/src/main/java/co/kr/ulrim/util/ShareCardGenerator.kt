package co.kr.ulrim.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat

object ShareCardGenerator {

    fun generateQuoteCard(
        context: Context,
        quoteText: String,
        backgroundResId: Int
    ): Bitmap {
        val width = 1080
        val height = 1920

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Draw background image
        val backgroundDrawable = ResourcesCompat.getDrawable(context.resources, backgroundResId, null)
        backgroundDrawable?.setBounds(0, 0, width, height)
        backgroundDrawable?.draw(canvas)

        // Draw overlay
        val overlayPaint = Paint().apply {
            color = Color.parseColor("#66000000") // 40% black
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), overlayPaint)

        // Draw quote text
        val textPaint = Paint().apply {
            color = Color.WHITE
            textSize = 56f
            typeface = Typeface.SERIF
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val maxWidth = width - 120 // 60px padding on each side
        val lines = wrapText(quoteText, textPaint, maxWidth.toFloat())
        
        val lineHeight = 80f
        val totalTextHeight = lines.size * lineHeight
        val startY = (height - totalTextHeight) / 2f + 200f // Slightly above center

        lines.forEachIndexed { index, line ->
            val y = startY + (index * lineHeight)
            canvas.drawText(line, width / 2f, y, textPaint)
        }

        // Draw watermark
        val watermarkPaint = Paint().apply {
            color = Color.parseColor("#80FFFFFF") // 50% white
            textSize = 32f
            typeface = Typeface.SERIF
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("Ulrim", width / 2f, height - 100f, watermarkPaint)

        return bitmap
    }

    private fun wrapText(text: String, paint: Paint, maxWidth: Float): List<String> {
        val lines = mutableListOf<String>()
        val words = text.split(" ")
        var currentLine = ""

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val testWidth = paint.measureText(testLine)

            if (testWidth > maxWidth && currentLine.isNotEmpty()) {
                lines.add(currentLine)
                currentLine = word
            } else {
                currentLine = testLine
            }
        }

        if (currentLine.isNotEmpty()) {
            lines.add(currentLine)
        }

        return lines
    }
}
