package ru.debian17.findme.data.model.attribute

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import ru.debian17.findme.data.model.edge.EdgeLocation

class LongAttributeInfo(@SerializedName("id") val id: Int,
                        @SerializedName("userId") val userId: Int,
                        @SerializedName("commentary") val comment: String,
                        @SerializedName("category") val categoryId: Int,
                        @SerializedName("edges") val edges: List<EdgeLocation>) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readInt(),
            parcel.createTypedArrayList(EdgeLocation)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(userId)
        parcel.writeString(comment)
        parcel.writeInt(categoryId)
        parcel.writeTypedList(edges)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LongAttributeInfo> {
        override fun createFromParcel(parcel: Parcel): LongAttributeInfo {
            return LongAttributeInfo(parcel)
        }

        override fun newArray(size: Int): Array<LongAttributeInfo?> {
            return arrayOfNulls(size)
        }
    }

}