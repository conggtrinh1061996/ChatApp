package com.dev.beginprojectandroid.utils

import com.dev.beginprojectandroid.BuildConfig
import timber.log.Timber

object Logger {
	
	const val TAG = "Chatting App"
	
	fun d(s: String?, vararg objects: Any?) {
		Timber.tag(TAG).d(s, *objects)
	}
	
	fun d(throwable: Throwable?, s: String?, vararg objects: Any?) {
		Timber.tag(TAG).d(throwable, s, *objects)
	}
	
	fun e(s: String?, vararg objects: Any?) {
		Timber.tag(TAG).e(s, *objects)
	}
	
	fun e(throwable: Throwable?, s: String?, vararg objects: Any?) {
		Timber.tag(TAG).e(throwable, s, *objects)
	}
	
	fun init() {
		if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
	}
}