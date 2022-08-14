package com.sottt.notificationdrawer.data.defined

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.sottt.notificationdrawer.R

class NotificationInfoAdapter(context: Context, sourceId: Int, data: List<NotificationInfo>) :
    ArrayAdapter<NotificationInfo>(context, sourceId, data) {

    private val id = sourceId

    inner class ViewHolder(val cardTitle: TextView, val cardContent: TextView)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val view = if (convertView != null) {
            viewHolder = convertView.tag as ViewHolder
            convertView
        } else {
            val view = LayoutInflater.from(context).inflate(id, parent, false)
            viewHolder = ViewHolder(
                view.findViewById(R.id.notification_card_title) as TextView,
                view.findViewById(R.id.notification_card_content) as TextView
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
        }
        return view
    }

}