package ru.debian17.findme.data.model.edge

import com.google.gson.annotations.SerializedName
import ru.debian17.findme.data.model.attribute.LongAttribute
import ru.debian17.findme.data.model.attribute.PointAttribute

class EdgeInfo(
    @SerializedName("point_attributes") val pointAttributes: List<PointAttribute>,
    @SerializedName("long_attributes") val longAttributes: List<LongAttribute>
)