package com.sottt.notificationdrawer

import android.graphics.Color
import android.support.v7.graphics.Palette
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

//    fun primaryColor(packageName: String): Color {
//        val bitmap = NotificationDrawerApplication.getAppIcon(packageName)
//        if (bitmap == null) {
//            return Color.valueOf(Color.TRANSPARENT);
//        } else {
//            val palette = Palette.from(bitmap).generate()
//        }
//    }

}