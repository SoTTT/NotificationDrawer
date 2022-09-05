package com.sottt.notificationdrawer.setting

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sottt.notificationdrawer.R

class SettingActivity : AppCompatActivity() {

    companion object {
        const val SETTINGS_PAGE = 0
        const val FILTER_PAGE = 1
        const val HISTORY_PAGE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        val page = savedInstanceState?.getInt("PAGE")
        if (page != null) {
            loadPage(page)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_activity_tool_bar_actions, menu)
        return true
    }

    private fun loadPage(page: Int) {
        when (page) {
            SETTINGS_PAGE -> {}
            FILTER_PAGE -> {

            }
            HISTORY_PAGE -> {

            }
        }
    }
}