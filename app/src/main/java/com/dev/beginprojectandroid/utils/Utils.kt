package com.dev.beginprojectandroid.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity

object Utils {
	fun hideKeyBoard(view: View, activity: FragmentActivity?) {
		view.clearFocus()
		try {
			val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
		} catch (e: Exception) {
		
		}
	}
	
	fun showKeyboard(view: View, activity: FragmentActivity?) {
		try {
			val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
		} catch (e: Exception) {
		
		}
	}
}