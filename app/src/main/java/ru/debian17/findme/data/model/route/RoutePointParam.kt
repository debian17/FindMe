package ru.debian17.findme.data.model.route

import com.google.gson.annotations.SerializedName

class RoutePointParam(@SerializedName("longitude") val longitude: Double,
                      @SerializedName("latitude") val latitude: Double)