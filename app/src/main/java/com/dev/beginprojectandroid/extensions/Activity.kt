package com.dev.beginprojectandroid.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dev.beginprojectandroid.utils.Constants

fun AppCompatActivity.showFragmentInActivity(
	fragment: Fragment, containerId: Int,
	openType: Int = Constants.REPLACE,
	addToBackstack: Boolean = true,
	backstackTag: String? = null
) {
	supportFragmentManager.beginTransaction()
		.apply {
			if (openType == Constants.REPLACE) {
				replace(containerId, fragment)
			} else add(containerId, fragment)
			
			if (addToBackstack) {
				addToBackStack(backstackTag)
			}
		}
		.commitAllowingStateLoss()
}