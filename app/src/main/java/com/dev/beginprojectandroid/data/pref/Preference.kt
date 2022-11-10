package com.dev.beginprojectandroid.data.pref

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

abstract class Preference(
	context: Context,
	private val name: String? = null

) {
	private val prefs: SharedPreferences by lazy {
		context.getSharedPreferences(name ?: javaClass.simpleName, Context.MODE_PRIVATE)
	}
	
	abstract class PrefDelegate<T>(val prefKey: String) {
		abstract operator fun getValue(thisRef: Any?, property: KProperty<*>): T
		abstract operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T)
	}
	
	enum class StorableType {
		String, Boolean
	}
	
	inner class GenericPrefDelegate<T>(
		prefKey: String? = null,
		private val defaultValue: T?,
		private val type: StorableType
	): PrefDelegate<T>(prefKey!!) {
		override fun getValue(thisRef: Any?, property: KProperty<*>): T {
			return when(type) {
				StorableType.String -> {
					prefs.getString(prefKey ?: property.name, defaultValue as String) as T
				}
				StorableType.Boolean -> {
					prefs.getBoolean(prefKey ?: property.name, defaultValue as Boolean) as T
				}
			}
		}
		
		override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
			when (type) {
				StorableType.String -> {
					prefs.edit().putString(prefKey ?: property.name, value as String).apply()
				}
				StorableType.Boolean -> {
					prefs.edit().putBoolean(prefKey ?: property.name, value as Boolean).apply()
				}
			}
		}
		
	}
	
	fun stringPref(prefKey: String? = null, defaultValue: String? = null) =
		GenericPrefDelegate(prefKey, defaultValue, StorableType.String)
	
	fun booleanPref(prefKey: String? = null, defaultValue: Boolean? = null) =
		GenericPrefDelegate(prefKey, defaultValue, StorableType.Boolean)
}