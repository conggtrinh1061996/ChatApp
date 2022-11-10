package com.dev.beginprojectandroid.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.dev.beginprojectandroid.extensions.showFragmentInActivity
import com.dev.beginprojectandroid.utils.Constants.ADD

abstract class BaseActivity<VB: ViewDataBinding, VM: BaseViewModel<*>> : AppCompatActivity() {
	protected lateinit var binding: VB
	protected abstract val viewModel: VM
	abstract fun layoutId(): Int
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = DataBindingUtil.setContentView(this, layoutId())
		binding.lifecycleOwner = this
	}
	
	fun openFragment(
		fragment: Fragment, containerId: Int,
		openType : Int = ADD, addToBackstack: Boolean,
		backstackTag: String?
	) {
		showFragmentInActivity(fragment, containerId, openType, addToBackstack, backstackTag)
	}
}