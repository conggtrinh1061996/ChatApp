package com.dev.beginprojectandroid.app

import android.app.Application
import com.dev.beginprojectandroid.data.pref.Preference
import com.dev.beginprojectandroid.data.pref.PreferenceHelper
import com.dev.beginprojectandroid.utils.Logger

class MainApplication: Application() {
	
	override fun onCreate() {
		super.onCreate()
		_instance = this
		// Init Timber in MainApplication
		Logger.init()
	}
	
	companion object {
		private lateinit var _instance: MainApplication
		val instance: MainApplication
			get() = _instance
	}
}