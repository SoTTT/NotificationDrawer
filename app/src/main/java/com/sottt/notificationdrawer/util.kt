package com.sottt.notificationdrawer

import android.widget.Toast
import com.sottt.notificationdrawer.NotificationDrawerApplication.Companion.applicationContext

object util {

    fun showToast(text: CharSequence, time: Int) {
        Toast.makeText(
            applicationContext(),
            text,
            time
        ).show()
    }

}