package com.sottt.notificationdrawer.setting

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.sottt.notificationdrawer.R

class SettingActivity : AppCompatActivity() {

    private val navController by lazy {
        Navigation.findNavController(this, R.id.center)
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(SettingViewModel::class.java)
    }

    companion object {
        const val SETTINGS_PAGE = 0
        const val FILTER_PAGE = 1
        const val HISTORY_PAGE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onStart() {
        super.onStart()
        val navInflater = navController.navInflater
        val navGraph = navInflater.inflate(R.navigation.settings_page_nav_host)
        val startDestination = when (this.intent.extras?.getInt("PAGE")) {
            SETTINGS_PAGE -> R.id.appSettingsFragment
            FILTER_PAGE -> R.id.filterFragment
            HISTORY_PAGE -> 2
            else -> R.id.appSettingsFragment
        }
        navGraph.startDestination = startDestination
        navController.graph = navGraph
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_activity_tool_bar_actions, menu)
        return true
    }

    private fun initViewModel() {
        viewModel
    }

    fun getActivityViewModel(): SettingViewModel {
        return viewModel
    }

    private fun loadFilter() {

    }

}