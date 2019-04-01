package ru.debian17.findme.app.dal

import com.google.android.gms.location.LocationCallback
import io.reactivex.Single
import org.osmdroid.util.GeoPoint
import ru.debian17.findme.data.model.route.RoutePoint

interface LocationDataSource {

    fun subscribe(locationCallback: LocationCallback)

    fun unsubscribe(locationCallback: LocationCallback)

    fun buildRoute(startPoint: GeoPoint, endPoint: GeoPoint): Single<List<RoutePoint>>

}