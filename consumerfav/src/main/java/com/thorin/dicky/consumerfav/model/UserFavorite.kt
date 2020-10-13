package com.thorin.dicky.consumerfav.model

import android.os.Parcel
import android.os.Parcelable

data class UserFavorite (
        var username: String? = "",
        var name: String? = "",
        var avatar: String? = "",
        var company: String? = "",
        var location: String? = "",
        var repository: String? = "",
        var followers: String? = "",
        var following: String? = "",
        var userFav: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(name)
        parcel.writeString(avatar)
        parcel.writeString(company)
        parcel.writeString(location)
        parcel.writeString(repository)
        parcel.writeString(followers)
        parcel.writeString(following)
        parcel.writeString(userFav)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserFavorite> {
        override fun createFromParcel(parcel: Parcel): UserFavorite {
            return UserFavorite(parcel)
        }

        override fun newArray(size: Int): Array<UserFavorite?> {
            return arrayOfNulls(size)
        }
    }
}