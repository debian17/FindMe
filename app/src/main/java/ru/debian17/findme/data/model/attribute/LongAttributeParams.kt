package ru.debian17.findme.data.model.attribute

import com.google.gson.annotations.SerializedName
import ru.debian17.findme.data.model.LocationParams

class LongAttributeParams(@SerializedName("category") val categoryId: Int,
                          @SerializedName("commentary") val comment: String,
                          @SerializedName("points") val points: List<LocationParams>)