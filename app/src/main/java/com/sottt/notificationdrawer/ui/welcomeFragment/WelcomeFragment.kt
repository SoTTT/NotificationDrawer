package com.sottt.notificationdrawer.ui.welcomeFragment

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.sottt.notificationdrawer.databinding.FragmentWelcomeBinding
import com.sottt.notificationdrawer.util
import java.lang.Exception


class WelcomeFragment : Fragment() {

    private var _viewBinding: FragmentWelcomeBinding? = null

    val viewBinding get() = _viewBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.readNotificationPermission.setOnClickListener {
            try {
                val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context?.startActivity(intent)
            } catch (exception: ActivityNotFoundException) {
                try {
                    val intent = Intent()
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val cn = ComponentName(
                        "com.android.settings",
                        "com.android.settings.Settings.NotificationAccessSettingsActivity"
                    )
                    intent.component = cn
                    intent.putExtra(":settings:show_fragment", "NotificationAccessSettings")
                    context?.startActivity(intent)
                } catch (exception: Exception) {
                    exception.printStackTrace()
                    util.showToast("跳转失败", Toast.LENGTH_LONG)
                }
            }
        }
    }



}