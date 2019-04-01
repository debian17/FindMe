package ru.debian17.findme.data.mapper

import org.osmdroid.util.GeoPoint
import ru.debian17.findme.data.model.route.RoutePoint


class RoutePointMapper : Mapper<RoutePoint, GeoPoint> {

    override fun map(obj: RoutePoint): GeoPoint {
        return GeoPoint(obj.latitude, obj.longitude)
    }

    fun mapAll(routePoints: List<RoutePoint>): List<GeoPoint> {
        val geoPoints = ArrayList<GeoPoint>()
        val routePointMapper = RoutePointMapper()
        routePoints.forEach { point ->
            val geoPoint = routePointMapper.map(point)
            geoPoints.add(geoPoint)
        }
        return geoPoints
    }

}