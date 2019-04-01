package ru.debian17.findme.data.model.route

import com.google.gson.annotations.SerializedName


class RouteInfo(@SerializedName("points") val points: List<RoutePoint>)