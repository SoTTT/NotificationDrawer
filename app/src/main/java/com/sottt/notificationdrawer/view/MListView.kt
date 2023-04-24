package com.sottt.notificationdrawer.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ListView
import kotlin.math.abs

class MListView(context: Context, attributes: AttributeSet) : ListView(context, attributes) {

    private var mLastX: Float = 0f
    private var mLastY: Float = 0f

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.x
                mLastY = ev.y
            }
            MotionEvent.ACTION_MOVE -> if (abs(mLastX - ev.x) > abs(mLastY - ev.y)) {
                return false
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {}
        }
        return super.onInterceptTouchEvent(ev)
    }
}