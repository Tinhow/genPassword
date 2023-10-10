package com.example.genPass

import android.os.Parcel
import android.os.Parcelable

data class Password(
    var description: String,
    var length: Int,
    var includeUppercase: Boolean,
    var includeNumbers: Boolean,
    var includeSpecialChars: Boolean
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(description)
        parcel.writeInt(length)
        parcel.writeByte(if (includeUppercase) 1 else 0)
        parcel.writeByte(if (includeNumbers) 1 else 0)
        parcel.writeByte(if (includeSpecialChars) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Password> {
        override fun createFromParcel(parcel: Parcel): Password {
            return Password(parcel)
        }

        override fun newArray(size: Int): Array<Password?> {
            return arrayOfNulls(size)
        }
    }
}
