package ru.debian17.findme.app.ui.menu.route.build

import android.location.Location
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.debian17.findme.app.mvp.BaseView

interface BuildRouteView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateLocation(location: Location)

}