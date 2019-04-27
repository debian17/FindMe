package ru.debian17.findme.data.model

import com.google.gson.annotations.SerializedName

class LocationParams(@SerializedName("latitude") val latitude: Double,
                     @SerializedName("longitude") val longitude: Double)