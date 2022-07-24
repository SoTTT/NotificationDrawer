package com.sottt.notificationdrawer.ui.homeFragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.sottt.notificationdrawer.MainActivity
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import com.sottt.notificationdrawer.data.defined.NotificationInfoAdapter
import com.sottt.notificationdrawer.databinding.FragmentHomeBinding
import java.util.*


class HomeFragment : Fragment() {

    private var _viewBinding: FragmentHomeBinding? = null

    private val viewBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    private fun iniView() {
        val list = mutableListOf<NotificationInfo>()
        for (index in 1..10) {
            list.add(NotificationInfo("123", "123456".repeat((Math.random() * 100).toInt())))
        }
        val adapter =
            NotificationInfoAdapter(
                this.activity as MainActivity,
                R.layout.notification_card,
                list
            )
        viewBinding.cardList.adapter = adapter
    }


}