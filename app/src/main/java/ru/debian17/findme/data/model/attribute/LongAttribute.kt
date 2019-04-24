package ru.debian17.findme.data.model.attribute

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class LongAttribute(
        @SerializedName("id") val id: Int,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("commentary") val comment: String,
        @SerializedName("category") val categoryId: Int,
        @SerializedName("source_long") val startLon: Double,
        @SerializedName("source_lat") val startLat: Double,
        @SerializedName("target_long") val endLon: Double,
        @SerializedName("target_lat") val endLat: Double
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(userId)
        parcel.writeString(comment)
        parcel.writeInt(categoryId)
        parcel.writeDouble(startLon)
        parcel.writeDouble(startLat)
        parcel.writeDouble(endLon)
        parcel.writeDouble(endLat)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LongAttribute> {
        override fun createFromParcel(parcel: Parcel): LongAttribute {
            return LongAttribute(parcel)
        }

        override fun newArray(size: Int): Array<LongAttribute?> {
            return arrayOfNulls(size)
        }
    }
}