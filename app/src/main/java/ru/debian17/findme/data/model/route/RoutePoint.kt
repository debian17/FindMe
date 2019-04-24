package ru.debian17.findme.data.model.route

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import ru.debian17.findme.data.model.attribute.Attribute

class RoutePoint(
    @SerializedName("edge_id") val edgeId: Int,
    @SerializedName("source_longitude") val startLong: Double,
    @SerializedName("source_latitude") val startLat: Double,
    @SerializedName("target_longitude") val endLong: Double,
    @SerializedName("target_latitude") val endLat: Double,
    @SerializedName("attributes") val attributes: List<Attribute>
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.createTypedArrayList(Attribute)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(edgeId)
        parcel.writeDouble(startLong)
        parcel.writeDouble(startLat)
        parcel.writeDouble(endLong)
        parcel.writeDouble(endLat)
        parcel.writeTypedList(attributes)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RoutePoint> {
        override fun createFromParcel(parcel: Parcel): RoutePoint {
            return RoutePoint(parcel)
        }

        override fun newArray(size: Int): Array<RoutePoint?> {
            return arrayOfNulls(size)
        }
    }
}