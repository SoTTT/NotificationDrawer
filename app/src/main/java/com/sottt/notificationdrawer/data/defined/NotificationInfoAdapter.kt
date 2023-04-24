package com.sottt.notificationdrawer.data.defined

import android.app.Activity
import android.content.Context
import android.os.Build
import android.view.Display
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.Util
import kotlin.math.abs

class NotificationInfoAdapter(context: Context, sourceId: Int, data: List<NotificationInfo>) :
    ArrayAdapter<NotificationInfo>(context, sourceId, data) {

    companion object {
        const val TAG = "NotificationInfoAdapter"
        const val MIN = 0;
    }

    private val id = sourceId
    private var lastX = 0f
    private var lastY = 0f
    private var pushX = 0f
    private var pushY = 0f

    @Suppress("DEPRECATION")
    private val height by lazy {
        if (Build.VERSION.SDK_INT >= 30)
            (context as Activity).windowManager.currentWindowMetrics.bounds.height()
        else {
            (context as Activity).windowManager.defaultDisplay.height
        }
    }

    @Suppress("DEPRECATION")
    private val width by lazy {
        if (Build.VERSION.SDK_INT >= 30)
            (context as Activity).windowManager.currentWindowMetrics.bounds.width()
        else {
            (context as Activity).windowManager.defaultDisplay.width
        }
    }

    inner class ViewHolder(
        val cardTitle: TextView,
        val cardContent: TextView,
        val smallIcon: ImageView
    )

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val view = if (convertView != null) {
            viewHolder = convertView.tag as ViewHolder
            convertView
        } else {
            val view = LayoutInflater.from(context).inflate(id, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.notification_card_title) as TextView,
                view.findViewById(R.id.notification_card_content) as TextView,
                view.findViewById(R.id.notification_card_small_icon) as ImageView
            )
            view.tag = viewHolder
            view
        }
//        val cardTitle = view.findViewById(R.id.notification_card_title) as TextView
//        val cardContent = view.findViewById(R.id.notification_card_content) as TextView
        val dataItem = getItem(position)
        if (dataItem != null) {
            viewHolder.cardTitle.text = dataItem.title
            viewHolder.cardContent.text = dataItem.content
            viewHolder.smallIcon.setImageBitmap(dataItem.smallIcon)
        }
        view.setOnTouchListener { outView, event ->
            val innerView = outView.findViewById<CardView>(R.id.card)
            innerView.setOnFocusChangeListener { viewSelf, hasFocus ->
                if (!hasFocus) {
                    viewSelf.invalidate()
                }
            }
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    pushX = event.rawX
                    pushY = event.rawY
                    lastX = event.rawX
                    lastY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    if (abs(lastX - event.rawX) > MIN) {
                        var left = innerView.left + (event.rawX - lastX)
                        if (left < 10) {
                            left = 10f
                        }
                        if (left + innerView.width > width - 10) {
                            left = (width - innerView.width - 10).toFloat()
                        }
                        innerView.layout(
                            left.toInt(),
                            innerView.top,
                            (left + innerView.width).toInt(),
                            innerView.bottom
                        )
                        lastX = event.rawX
                        lastY = event.rawY
                    }

                }
                MotionEvent.ACTION_UP -> {
                    if (abs(pushX - lastX) <= MIN || abs(pushY - lastY) <= 10) {
                        view.performClick()
                    }
                }
                else -> {}
            }
            true
        }

        return view
    }

}