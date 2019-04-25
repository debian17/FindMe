package ru.debian17.findme.data.model.category

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

class Category(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("is_point") val isPoint: Boolean,
    @SerializedName("is_long") val isLong: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeByte(if (isPoint) 1 else 0)
        parcel.writeByte(if (isLong) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}