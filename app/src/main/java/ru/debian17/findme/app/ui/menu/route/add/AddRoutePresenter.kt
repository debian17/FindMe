package ru.debian17.findme.app.ui.menu.route.add

import android.location.Location
import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.osmdroid.util.GeoPoint
import org.threeten.bp.OffsetDateTime
import ru.debian17.findme.app.dal.LocationDataSource
import ru.debian17.findme.app.ext.observeOnUI
import ru.debian17.findme.app.ext.subscribeOnIO
import ru.debian17.findme.app.mvp.BasePresenter
import ru.debian17.findme.data.model.LocationParams
import ru.debian17.findme.data.model.route.RouteDotParam
import ru.debian17.findme.data.model.route.RoutePointParam
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

@InjectViewState
class AddRoutePresenter(private val locationDataSource: LocationDataSource) : BasePresenter<AddRouteView>() {

    companion object {
        private const val MIN_DISTANCE = 10f
    }

    val points = ArrayList<RouteDotParam>()

    private val locationSubject: PublishSubject<Location> = PublishSubject.create()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            if (p0 != null) {
                viewState.showMain()
                val location = p0.lastLocation
                if (points.isEmpty()) {
                    val routePointParam = RoutePointParam(location.longitude, location.latitude)
                    val routeDot = RouteDotParam(routePointParam, Date())
                    points.add(routeDot)
                }
                viewState.onLocationChanged(location)
                locationSubject.onNext(p0.lastLocation)
            }
        }
    }

    override fun onFirstViewAttach() {
        viewState.showLoading()
        locationDataSource.subscribe(locationCallback)
        unsubscribeOnDestroy(locationSubject
                .subscribeOn(Schedulers.io())
                .skip(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onLocationChanged, this::onError))
    }

    private fun onLocationChanged(location: Location) {
        if (points.isNotEmpty()) {
            val lastPoint = points.last()
            val start = GeoPoint(lastPoint.location.latitude, lastPoint.location.longitude)
            val end = GeoPoint(location.latitude, location.longitude)
            val distance = start.distanceToAsDouble(end)

            if (distance > MIN_DISTANCE) {
                val routePointParam = RoutePointParam(location.longitude, location.latitude)
                val routeDot = RouteDotParam(routePointParam, Date())
                points.add(routeDot)
            }
        }
    }

    private fun onError(throwable: Throwable) {
        viewState.showMain()
        viewState.onAddRouteSuccess()
    }

    override fun onDestroy() {
        super.onDestroy()
        locationDataSource.unsubscribe(locationCallback)
    }

    fun saveRoute() {
        viewState.showLoading()
        locationDataSource.unsubscribe(locationCallback)
        unsubscribeOnDestroy(locationDataSource.addRoute(points)
                .subscribeOnIO()
                .doOnError {
                    errorBody = getError(it)
                }
                .observeOnUI()
                .subscribe(this::onAddRouteSuccess, this::onError))
    }

    private fun onAddRouteSuccess() {
        viewState.showMain()
        viewState.onAddRouteSuccess()
    }

}