package ru.debian17.findme.app.mvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.ViewCommand
import com.arellomobile.mvp.viewstate.strategy.StateStrategy

class SingleCommandStrategy : StateStrategy {

    override fun <View : MvpView?> beforeApply(
        currentState: MutableList<ViewCommand<View>>?,
        incomingCommand: ViewCommand<View>?
    ) {
        if (incomingCommand != null && currentState != null) {
            val iterator = currentState.iterator()
            while (iterator.hasNext()) {
                val curCommnad = iterator.next()
                if (incomingCommand.strategyType == curCommnad.strategyType) {
                    iterator.remove()
                    break
                }
            }
            currentState.add(incomingCommand)
        }
    }

    override fun <View : MvpView?> afterApply(
        currentState: MutableList<ViewCommand<View>>?,
        incomingCommand: ViewCommand<View>?
    ) {

    }
}