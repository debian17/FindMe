package ru.debian17.findme.data.model.edge

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class EdgeLocation(@SerializedName("source_long") val startLon: Double,
                   @SerializedName("source_lat") val startLat: Double,
                   @SerializedName("target_long") val endLon: Double,
                   @SerializedName("target_lat") val endLat: Double,
                   @SerializedName("length") val length: Double) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(startLon)
        parcel.writeDouble(startLat)
        parcel.writeDouble(endLon)
        parcel.writeDouble(endLat)
        parcel.writeDouble(length)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EdgeLocation> {
        override fun createFromParcel(parcel: Parcel): EdgeLocation {
            return EdgeLocation(parcel)
        }

        override fun newArray(size: Int): Array<EdgeLocation?> {
            return arrayOfNulls(size)
        }
    }

}