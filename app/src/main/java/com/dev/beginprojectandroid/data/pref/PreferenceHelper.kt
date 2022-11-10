package com.dev.beginprojectandroid.data.pref

import android.content.Context
import com.dev.beginprojectandroid.app.MainApplication
import com.dev.beginprojectandroid.utils.Constants

class PreferenceHelper(context: Context): Preference(context, Constants.PREF_NAME) {
	
	var userAccount by stringPref("user_account", "")
	var userPassword by stringPref("user_password", "")
	var isSignIn by booleanPref("is_signed_in", false)
	
	companion object {
		private var instance: PreferenceHelper? = null
		fun getInstance(): PreferenceHelper = instance ?: PreferenceHelper(MainApplication.instance)
	}
}