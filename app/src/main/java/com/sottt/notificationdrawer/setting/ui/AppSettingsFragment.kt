package com.sottt.notificationdrawer.setting.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.sottt.notificationdrawer.R

class AppSettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}