package ru.debian17.findme.app.mvp

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import ru.debian17.findme.R
import ru.debian17.findme.app.androidx.MvpAndroidxFragment
import ru.debian17.findme.app.ui.main.MainActivity
import ru.debian17.findme.app.ui.main.MainNavigator

abstract class BaseFragment : MvpAndroidxFragment() {

    protected lateinit var navigator: MainNavigator

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))

        navigator = (activity as MainActivity).getMainNavigator()

    }

}