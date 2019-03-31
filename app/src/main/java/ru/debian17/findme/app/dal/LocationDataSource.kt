package ru.debian17.findme.app.dal

import com.google.android.gms.location.LocationCallback

interface LocationDataSource {

    fun subscribe(locationCallback: LocationCallback)

    fun unsubscribe(locationCallback: LocationCallback)

}