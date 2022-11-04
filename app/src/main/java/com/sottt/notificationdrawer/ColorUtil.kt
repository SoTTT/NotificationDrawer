package com.sottt.notificationdrawer

import android.graphics.Bitmap
import android.graphics.Color
import androidx.palette.graphics.Palette
import kotlin.math.floor

object ColorUtil {

    /**
     * 颜色加深算法
     */
    private fun setColorBurn(rgb: Int, `val`: Float): Int {
        var r = rgb shr 16 and 0xff
        var g = rgb shr 8 and 0xff
        var b = rgb and 0xff
        r = floor((r * (1f - `val`)).toDouble()).toInt()
        g = floor((g * (1f - `val`)).toDouble()).toInt()
        b = floor((b * (1f - `val`)).toDouble()).toInt()
        return Color.rgb(r, g, b)
    }

    /**
     * 颜色浅化算法
     */
    private fun setColorShallow(rgb: Int, `val`: Float): Int {
        var r = rgb shr 16 and 0xff
        var g = rgb shr 8 and 0xff
        var b = rgb and 0xff
        r = floor((r * (1f + `val`)).toDouble()).toInt()
        g = floor((g * (1f + `val`)).toDouble()).toInt()
        b = floor((b * (1f + `val`)).toDouble()).toInt()
        return Color.rgb(r, g, b)
    }

    fun isValidIconColor(color: Color): Boolean {
        return color != Color.valueOf(Color.TRANSPARENT)
    }

    fun primaryColor(packageName: String): Color {
        val bitmap = NotificationDrawerApplication.getAppIcon(packageName)
        return primaryColor(bitmap)
    }

    private fun primaryColor(icon: Bitmap?):Color {
        return if (icon == null) {
            Color.valueOf(Color.TRANSPARENT)
        } else {
            val palette = Palette.from(icon).generate()
            val rgb = palette.lightVibrantSwatch?.rgb
            return if (rgb == null) {
                Color.valueOf(Color.TRANSPARENT)
            } else {
                Color.valueOf(rgb)
            }
        }
    }

}