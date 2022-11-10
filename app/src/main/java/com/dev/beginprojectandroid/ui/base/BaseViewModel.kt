package com.dev.beginprojectandroid.ui.base

import androidx.lifecycle.ViewModel
import java.lang.ref.WeakReference

abstract class BaseViewModel<N> : ViewModel() {
	private lateinit var mNavigatior: WeakReference<N>
	
	fun getNavigator(): N {
		return this.mNavigatior.get()!!
	}
	
	fun setNavigator(navigator: N) {
		this.mNavigatior = WeakReference(navigator)
	}
}