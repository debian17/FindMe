package ru.debian17.findme.data.model.attribute

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class PointAttribute(
        @SerializedName("id") val id: Int,
        @SerializedName("user_id") val userId: Int,
        @SerializedName("commentary") val comment: String,
        @SerializedName("category") val categoryId: Int,
        @SerializedName("radius") val radius: Int,
        @SerializedName("longitude") val longitude: Double,
        @SerializedName("latitude") val latitude: Double
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readInt(),
            parcel.readDouble(),
            parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(userId)
        parcel.writeString(comment)
        parcel.writeInt(categoryId)
        parcel.writeInt(radius)
        parcel.writeDouble(longitude)
        parcel.writeDouble(latitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PointAttribute> {
        override fun createFromParcel(parcel: Parcel): PointAttribute {
            return PointAttribute(parcel)
        }

        override fun newArray(size: Int): Array<PointAttribute?> {
            return arrayOfNulls(size)
        }
    }
}