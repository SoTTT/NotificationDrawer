package com.sottt.notificationdrawer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.sottt.notificationdrawer.ui.welcomeFragment.WelcomeFragment

class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        if (Util.checkNecessaryPermission()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.extras
            startActivity(intent)
        } else {
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.welcome_activity_center, WelcomeFragment())
            transaction.commit()
        }
    }

}