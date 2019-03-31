package ru.debian17.findme.data.repository

import android.content.Context
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import ru.debian17.findme.app.dal.LocationDataSource

class LocationRepository(context: Context) : LocationDataSource {

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

}