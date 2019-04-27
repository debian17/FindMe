package ru.debian17.findme.data.model.attribute

import com.google.gson.annotations.SerializedName
import ru.debian17.findme.data.model.LocationParams

class PointAttributeParams(@SerializedName("category") val categoryid: Int,
                           @SerializedName("radius") val radius: Double,
                           @SerializedName("commentary") val comment: String,
                           @SerializedName("location") val location: LocationParams)