package com.dev.beginprojectandroid.data.model

import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator

class User(
	var uid: String = "",
	var userName: String = ""
) : Parcelable {
	
	constructor(parcel: Parcel): this (
		parcel.readString().toString(),
		parcel.readString().toString()
	)
	
	override fun describeContents(): Int {
		return 0
	}
	
	override fun writeToParcel(parcel: Parcel, p1: Int) {
		parcel.writeString(uid)
		parcel.writeString(userName)
	}
	
	companion object CREATOR : Creator<User> {
		override fun createFromParcel(parcel: Parcel): User {
			return User(parcel)
		}
		
		override fun newArray(size: Int): Array<User?> {
			return arrayOfNulls(size)
		}
	}
}