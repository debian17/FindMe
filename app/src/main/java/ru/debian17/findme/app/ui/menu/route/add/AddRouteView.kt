package ru.debian17.findme.app.ui.menu.route.add

import android.location.Location
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.debian17.findme.app.mvp.BaseView

interface AddRouteView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onLocationChanged(location: Location)

    fun onAddRouteSuccess()

}