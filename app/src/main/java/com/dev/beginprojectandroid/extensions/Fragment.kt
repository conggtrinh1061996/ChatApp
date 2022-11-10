package com.dev.beginprojectandroid.extensions

import androidx.fragment.app.Fragment
import com.dev.beginprojectandroid.utils.Constants.REPLACE

fun Fragment.showFragment(
	fragment: Fragment, containerId: Int,
	openType: Int = REPLACE,
	addToBackstack: Boolean = true,
	backstackTag: String? = null
) {
	childFragmentManager.beginTransaction()
		.apply {
			if (openType == REPLACE) {
				replace(containerId, fragment)
			} else add(containerId, fragment)
			
			if (addToBackstack) {
				addToBackStack(backstackTag)
			}
		}
		.commitAllowingStateLoss()
}