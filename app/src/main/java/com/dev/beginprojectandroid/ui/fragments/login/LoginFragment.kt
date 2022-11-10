package com.dev.beginprojectandroid.ui.fragments.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.pref.PreferenceHelper
import com.dev.beginprojectandroid.databinding.FragmentLoginBinding
import com.dev.beginprojectandroid.ui.activity.MainActivity
import com.dev.beginprojectandroid.ui.base.BaseFragment
import com.dev.beginprojectandroid.ui.fragments.lastmessenger.LatestMessengerFragment
import com.dev.beginprojectandroid.utils.Constants.ADD
import com.dev.beginprojectandroid.utils.Logger
import com.dev.beginprojectandroid.utils.Utils
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>() {
	
	override val viewModel: LoginViewModel by viewModels()
	
	override fun layoutId(): Int = R.layout.fragment_login
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// sign in
		signInWithEmailAndPassword()
		// click back to register
		backToRegister()
	}
	
	private fun backToRegister() {
		binding.backToRegister.setOnClickListener {
			parentFragmentManager.popBackStack()
		}
	}
	
	/**
	 * Đăng nhập với email và password
	 */
	private fun signInWithEmailAndPassword() {
		binding.btnLogin.setOnClickListener {
			Utils.hideKeyBoard(it, activity) // hide keyboard
			if (binding.edtEmailLogin.text.toString().isEmpty() || binding.edtPasswordLogin.text.toString().isEmpty()) {
				Toast.makeText(requireContext(), requireContext().getString(R.string.toast_email_or_password_invalid), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			// Kiểm tra tài khoản khi đăng nhập
			FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.edtEmailLogin.text.toString(), binding.edtPasswordLogin.text.toString())
				.addOnCompleteListener { result ->
					if (result.isSuccessful) {
						PreferenceHelper.getInstance().isSignIn = true
						Logger.d("Sign in is successfully!!!")
						(activity as MainActivity).openFragment(
							LatestMessengerFragment.newInstance(), R.id.mainContainer, ADD, true, null
						)
						// Save account and password to sharePreference
						PreferenceHelper.getInstance().userAccount = binding.edtEmailLogin.text.toString()
						PreferenceHelper.getInstance().userPassword = binding.edtPasswordLogin.text.toString()
					} else {
						Logger.d("Sign in is failed.")
						Toast.makeText(requireContext(), requireContext().getString(R.string.toast_email_or_password_invalid), Toast.LENGTH_SHORT).show()
					}
				}
		}
	}
	
	companion object {
		fun newInstance(): Fragment {
			val fragment = LoginFragment()
			fragment.arguments = Bundle()
			return fragment
		}
	}
}