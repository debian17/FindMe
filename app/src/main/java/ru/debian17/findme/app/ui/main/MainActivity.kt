package ru.debian17.findme.app.ui.main

import android.os.Bundle
import android.preference.PreferenceManager
import org.osmdroid.config.Configuration
import ru.debian17.findme.R
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.app.ui.auth.AuthFragment

class MainActivity : BaseActivity() {

    private lateinit var mainNavigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(applicationContext,
                PreferenceManager.getDefaultSharedPreferences(applicationContext))

        setContentView(R.layout.activity_main)

        mainNavigator = MainNavigator(supportFragmentManager)
        mainNavigator.replaceScreen(AuthFragment.TAG)
    }

    fun getMainNavigator(): MainNavigator {
        return mainNavigator
    }

}
