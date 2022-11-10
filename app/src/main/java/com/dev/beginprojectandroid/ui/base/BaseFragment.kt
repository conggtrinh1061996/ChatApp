package com.dev.beginprojectandroid.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dev.beginprojectandroid.extensions.showFragment

abstract class BaseFragment<VB: ViewDataBinding, VM: BaseViewModel<*>> : Fragment() {
	
	protected lateinit var binding: VB
	protected abstract val viewModel: VM
	
	abstract fun layoutId(): Int
	
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		binding = DataBindingUtil.inflate(inflater, layoutId(), container, false)
		binding.lifecycleOwner = viewLifecycleOwner
		return binding.root
	}
	
	fun pushFragment(
		fragment: Fragment, containerId: Int,
		openType: Int, addToBackstack: Boolean,
		backstackTag: String?
	) {
		showFragment(fragment = fragment, containerId = containerId, openType, addToBackstack, backstackTag)
	}
}