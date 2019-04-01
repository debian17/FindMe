package ru.debian17.findme.data.model.route

import com.google.gson.annotations.SerializedName

class RoutePoint(@SerializedName("edge_id") val edgeId: Int,
                 @SerializedName("longitude") val longitude: Double,
                 @SerializedName("latitude") val latitude: Double)