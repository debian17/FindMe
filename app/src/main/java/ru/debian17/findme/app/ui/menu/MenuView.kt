package ru.debian17.findme.app.ui.menu

import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.debian17.findme.app.mvp.BaseView

interface MenuView : BaseView {

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onLogOutSuccess()

}