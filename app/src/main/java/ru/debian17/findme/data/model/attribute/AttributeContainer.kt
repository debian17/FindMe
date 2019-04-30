package ru.debian17.findme.data.model.attribute

import com.google.gson.annotations.SerializedName

class AttributeContainer(@SerializedName("point_attributes") val pointAttributes: List<PointAttribute>,
                         @SerializedName("long_attributes") val longAttributes: List<LongAttributeInfo>)