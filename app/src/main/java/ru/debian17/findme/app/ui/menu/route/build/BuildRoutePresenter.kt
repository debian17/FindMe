package ru.debian17.findme.app.ui.menu.route.build

import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import org.osmdroid.util.GeoPoint
import ru.debian17.findme.app.dal.LocationDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter
import ru.debian17.findme.data.model.route.RoutePoint
import ru.debian17.findme.data.network.error.ErrorCode

@InjectViewState
class BuildRoutePresenter(private val locationDataSource: LocationDataSource) : BasePresenter<BuildRouteView>() {

    private val updateCurrentLocationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 != null) {
                viewState.updateLocation(p0.lastLocation)
            }
        }
    }

    override fun onFirstViewAttach() {
        locationDataSource.subscribe(updateCurrentLocationCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        locationDataSource.unsubscribe(updateCurrentLocationCallback)
    }

    fun buildRoute(startPoint: GeoPoint, endPoint: GeoPoint) {
        viewState.showLoading()
        unsubscribeOnDestroy(locationDataSource.buildRoute(startPoint, endPoint)
                .subscribeOnIO()
                .doOnError {
                    errorBody = getError(it)
                }
                .observeOnUI()
                .subscribe(this::onRouteSuccess, this::onRouteError))
    }

    private fun onRouteSuccess(routePoints: List<RoutePoint>) {
        viewState.showMain()
        viewState.onBuildRoute(routePoints)
    }

    private fun onRouteError(throwable: Throwable) {
        viewState.showMain()
        viewState.onBuildRouteError(errorBody?.code ?: ErrorCode.UNKNOWN_ERROR)
    }

}