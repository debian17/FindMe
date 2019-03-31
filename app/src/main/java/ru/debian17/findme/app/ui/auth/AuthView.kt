package ru.debian17.findme.app.ui.auth

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.debian17.findme.app.mvp.BaseView

interface AuthView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun showMenu()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun checkPermission()

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onAuthError(code: Int)

}