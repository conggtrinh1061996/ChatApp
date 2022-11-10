package com.dev.beginprojectandroid.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.pref.PreferenceHelper
import com.dev.beginprojectandroid.databinding.ActivityMainBinding
import com.dev.beginprojectandroid.ui.base.BaseActivity
import com.dev.beginprojectandroid.ui.fragments.lastmessenger.LatestMessengerFragment
import com.dev.beginprojectandroid.ui.fragments.login.LoginFragment
import com.dev.beginprojectandroid.ui.fragments.register.RegisterFragment
import com.dev.beginprojectandroid.utils.Constants.ADD
import com.dev.beginprojectandroid.utils.Constants.REPLACE
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
	
	override val viewModel: MainViewModel by viewModels()
	
	override fun layoutId(): Int = R.layout.activity_main
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setUp()
	}
	
	/**
	 * Cài đặt
	 */
	private fun setUp() {
		if (PreferenceHelper.getInstance().isSignIn) {
			autoSignIn()
		} else {
			// Add register fragment to mainContainer
			openFragment(RegisterFragment.newInstance(), R.id.mainContainer, ADD, true, null)
		}
	}
	
	private fun autoSignIn() {
		val account = PreferenceHelper.getInstance().userAccount
		val password = PreferenceHelper.getInstance().userPassword
		FirebaseAuth.getInstance().signInWithEmailAndPassword(account, password)
			.addOnCompleteListener {
				if (it.isSuccessful) {
					openFragment(LatestMessengerFragment.newInstance(), R.id.mainContainer, REPLACE, true, null)
				} else {
					Toast.makeText(this, getString(R.string.toast_email_or_password_invalid), Toast.LENGTH_SHORT).show()
				}
			}
	}
}