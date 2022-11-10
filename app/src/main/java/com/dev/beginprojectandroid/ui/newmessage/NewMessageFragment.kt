package com.dev.beginprojectandroid.ui.newmessage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.model.User
import com.dev.beginprojectandroid.databinding.FragmentNewMessageBinding
import com.dev.beginprojectandroid.ui.activity.MainActivity
import com.dev.beginprojectandroid.ui.adapter.ItemMessengerAdapter
import com.dev.beginprojectandroid.ui.base.BaseFragment
import com.dev.beginprojectandroid.ui.fragments.chattingmessage.ChattingMessageFragment
import com.dev.beginprojectandroid.utils.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewMessageFragment : BaseFragment<FragmentNewMessageBinding, NewMessageViewModel>() {
	override val viewModel: NewMessageViewModel by viewModels()
	
	override fun layoutId(): Int = R.layout.fragment_new_message
	
	private var adapterUser: ItemMessengerAdapter? = null
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUpAdapter()
		fetchUserFromFirebase()
		//
		binding.btnBack.setOnClickListener {
			parentFragmentManager.popBackStack()
		}
	}
	
	private fun setUpAdapter() {
		adapterUser = ItemMessengerAdapter(requireContext())
		binding.recyclerViewAllUsers.adapter = adapterUser
		//
		adapterUser?.onItemClick = { user ->
			(activity as MainActivity).openFragment(
				ChattingMessageFragment.newInstance(user), R.id.mainContainer,
				Constants.ADD, true, null
			)
		}
	}
	
	private fun fetchUserFromFirebase() {
		val ref = FirebaseDatabase.getInstance().getReference("/users")
		val users: MutableList<User> = mutableListOf()
		ref.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				snapshot.children.forEach {
					val user = it.getValue(User::class.java)
					user?.let { data ->
						users.add(data)
					}
				}
				adapterUser?.setListUser(users)
			}
			
			override fun onCancelled(error: DatabaseError) {
			}
			
		})
	}
	
	companion object {
		fun newInstance(): Fragment {
			val fragment = NewMessageFragment()
			val args = Bundle()
			fragment.arguments = args
			return fragment
		}
	}
}