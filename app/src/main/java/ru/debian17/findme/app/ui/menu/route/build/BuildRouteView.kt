package ru.debian17.findme.app.ui.menu.route.build

import android.location.Location
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.debian17.findme.app.mvp.BaseView
import ru.debian17.findme.data.model.edge.EdgeInfo
import ru.debian17.findme.data.model.route.RoutePoint

interface BuildRouteView : BaseView {

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun updateLocation(location: Location)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onBuildRouteError(code: Int)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun onBuildRoute(routePoints: List<RoutePoint>)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onAttributesOfEdgeError(cde: Int)

    @StateStrategyType(OneExecutionStateStrategy::class)
    fun onEdgeClick(edgeInfo: EdgeInfo)

}