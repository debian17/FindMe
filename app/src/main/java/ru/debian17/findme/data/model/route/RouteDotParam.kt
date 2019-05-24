package ru.debian17.findme.data.model.route

import com.google.gson.annotations.SerializedName
import java.util.*

class RouteDotParam(@SerializedName("location") val location: RoutePointParam,
                    @SerializedName("time") val time: Date)