package com.hillwar.web

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class Image (val author: String?, val photo: Bitmap?, val url: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readParcelable(Bitmap::class.java.classLoader),
        parcel.readString().toString()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest!!.writeStringArray(arrayOf(author, photo.toString(), url))
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}