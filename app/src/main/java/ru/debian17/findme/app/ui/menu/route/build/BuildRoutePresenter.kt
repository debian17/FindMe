package ru.debian17.findme.app.ui.menu.route.build

import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import ru.debian17.findme.app.dal.LocationDataSource
import ru.debian17.findme.app.mvp.BasePresenter

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

}