package ru.debian17.findme.app.util


object DistanceUtil {

    fun roundDistance(meters: Double, n: Int): Double {
        var km = meters
        return if (meters >= 1000) {
            km = meters / 1000
            val scale = Math.pow(10.0, n.toDouble())
            Math.round(km * scale) / scale
        } else {
            return Math.round(km).toDouble()
        }
    }

}