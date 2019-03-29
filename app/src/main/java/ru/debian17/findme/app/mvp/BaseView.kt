package ru.debian17.findme.app.mvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

interface BaseView : MvpView {

    @StateStrategyType(SingleCommandStrategy::class)
    fun showLoading()

    @StateStrategyType(SingleCommandStrategy::class)
    fun showMain()

    @StateStrategyType(SingleCommandStrategy::class)
    fun showError(errorMessage: String?)

}