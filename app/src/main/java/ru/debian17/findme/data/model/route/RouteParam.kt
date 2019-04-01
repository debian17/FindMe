package ru.debian17.findme.data.model.route

import com.google.gson.annotations.SerializedName

class RouteParam(@SerializedName("source_location") val startPointResponse: RoutePointParam,
                 @SerializedName("target_location") val endPointResponse: RoutePointParam)