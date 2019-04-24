package ru.debian17.findme.data.model.attribute

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Attribute(
    @SerializedName("id") val id: Int,
    @SerializedName("is_point") val isPoint: Boolean
): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeByte(if (isPoint) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attribute> {
        override fun createFromParcel(parcel: Parcel): Attribute {
            return Attribute(parcel)
        }

        override fun newArray(size: Int): Array<Attribute?> {
            return arrayOfNulls(size)
        }
    }
}