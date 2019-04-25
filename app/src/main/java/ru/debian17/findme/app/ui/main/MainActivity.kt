package ru.debian17.findme.app.ui.main

import android.os.Bundle
import android.preference.PreferenceManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import org.osmdroid.config.Configuration
import ru.debian17.findme.R
import ru.debian17.findme.app.App
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BaseActivity
import ru.debian17.findme.app.ui.auth.AuthFragment

class MainActivity : BaseActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    @ProvidePresenter
    fun providePreenter(): MainPresenter {
        return MainPresenter(
            (applicationContext as App)
                .getDataSourceComponent()
                .provideCategoriesRepository()
        )
    }

    private lateinit var mainNavigator: MainNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )

        setContentView(R.layout.activity_main)

        mainNavigator = MainNavigator(supportFragmentManager)
        mainNavigator.replaceScreen(AuthFragment.TAG)

    }

    fun getMainNavigator(): MainNavigator {
        return mainNavigator
    }

    override fun showLoading() {

    }

    override fun showMain() {

    }

    override fun showError(errorMessage: String?) {

    }

}
