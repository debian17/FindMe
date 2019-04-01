package ru.debian17.findme.data.repository

import android.content.Context
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import io.reactivex.Single
import org.osmdroid.util.GeoPoint
import ru.debian17.findme.app.dal.LocationDataSource
import ru.debian17.findme.data.model.route.RouteParam
import ru.debian17.findme.data.model.route.RoutePoint
import ru.debian17.findme.data.model.route.RoutePointParam
import ru.debian17.findme.data.network.WebAPIService

class LocationRepository(context: Context,
                         private val webAPIService: WebAPIService) : LocationDataSource {

    companion object {
        const val DEFAULT_INTERVAL = 100L
    }

    private val locationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = DEFAULT_INTERVAL
    }

    @Throws(SecurityException::class)
    override fun subscribe(locationCallback: LocationCallback) {
        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    override fun unsubscribe(locationCallback: LocationCallback) {
        locationClient.removeLocationUpdates(locationCallback)
    }

    override fun buildRoute(startPoint: GeoPoint, endPoint: GeoPoint): Single<List<RoutePoint>> {
        val startPointParam = RoutePointParam(startPoint.longitude, startPoint.latitude)
        val endPointParam = RoutePointParam(endPoint.longitude, endPoint.latitude)
        val routeParams = RouteParam(startPointParam, endPointParam)
        return webAPIService.buildRoute(routeParams)
    }

}