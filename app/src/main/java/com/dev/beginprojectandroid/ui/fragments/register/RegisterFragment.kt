package com.dev.beginprojectandroid.ui.fragments.register

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.model.User
import com.dev.beginprojectandroid.databinding.FragmentRegisterBinding
import com.dev.beginprojectandroid.ui.activity.MainActivity
import com.dev.beginprojectandroid.ui.base.BaseFragment
import com.dev.beginprojectandroid.ui.fragments.login.LoginFragment
import com.dev.beginprojectandroid.utils.Constants.ADD
import com.dev.beginprojectandroid.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>() {
	override val viewModel: RegisterViewModel by viewModels()
	
	override fun layoutId(): Int = R.layout.fragment_register
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// Open Login Screen
		openLoginScreen()
		// Create Email and password
		binding.btnRegister.setOnClickListener {
			if (binding.edtEmail.text.toString().isEmpty() || binding.edtPassword.text.toString().isEmpty()) {
				Toast.makeText(requireContext(), requireContext().getString(R.string.toast_email_or_password_empty), Toast.LENGTH_SHORT).show()
				return@setOnClickListener
			}
			createEmailAndPassword(binding.edtEmail.text.toString(), binding.edtPassword.text.toString())
		}
	}
	
	/**
	 * Open Login Screen
	 */
	private fun openLoginScreen() {
		binding.txtAlreadyAccount.setOnClickListener {
			(activity as MainActivity).openFragment(LoginFragment.newInstance(), R.id.mainContainer, ADD, true, null)
		}
	}
	
	/**
	 * Tạo Email và password sau khi click button Register
	 */
	private fun createEmailAndPassword(email: String, password: String) {
		FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
			// Xử lí khi tạo thành công tài khoản
			.addOnCompleteListener {
				if (!it.isSuccessful) return@addOnCompleteListener
				// it susscessful
				Logger.d("Successfully created user with uid: ${it.result.user?.uid}")
				// Lưu thông tin của người dùng sau khi đăng kí
				saveUserToFirebaseDatabase()
				// Chuyển sang màn Login
				(activity as MainActivity).openFragment(LoginFragment.newInstance(), R.id.mainContainer, ADD, true, null)
			}
			// Xử lí khi không tạo được tài khoản
			.addOnFailureListener {
				Logger.d("Failed to create user: ${it.message}")
				Toast.makeText(requireContext(), requireContext().getString(R.string.toast_email_or_password_empty), Toast.LENGTH_SHORT).show()
			}
	}
	
	/**
	 * Lưu thông tin user: username, email, password
	 */
	private fun saveUserToFirebaseDatabase() {
		val uid = FirebaseAuth.getInstance().uid ?: ""
		val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
		
		val user = User(uid, binding.edtUserName.text.toString())
		ref.setValue(user)
			.addOnSuccessListener {
				Logger.d("Finally we save the user to Firebase Database")
			}
			.addOnFailureListener {
				Logger.d("Failed to save user to firebase database")
			}
	}
	
	companion object {
		fun newInstance(): Fragment {
			val fragment = RegisterFragment()
			fragment.arguments = Bundle()
			return fragment
		}
	}
}