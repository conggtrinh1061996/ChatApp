package com.dev.beginprojectandroid.ui.fragments.lastmessenger

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.model.Message
import com.dev.beginprojectandroid.data.pref.PreferenceHelper
import com.dev.beginprojectandroid.databinding.FragmentLatestMessengerBinding
import com.dev.beginprojectandroid.ui.activity.MainActivity
import com.dev.beginprojectandroid.ui.adapter.ItemMessengerAdapter
import com.dev.beginprojectandroid.ui.adapter.LatestMessageAdapter
import com.dev.beginprojectandroid.ui.base.BaseFragment
import com.dev.beginprojectandroid.ui.fragments.chattingmessage.ChattingMessageFragment
import com.dev.beginprojectandroid.ui.fragments.login.LoginFragment
import com.dev.beginprojectandroid.ui.fragments.register.RegisterFragment
import com.dev.beginprojectandroid.ui.newmessage.NewMessageFragment
import com.dev.beginprojectandroid.utils.Constants.ADD
import com.dev.beginprojectandroid.utils.Constants.REPLACE
import com.dev.beginprojectandroid.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class LatestMessengerFragment: BaseFragment<FragmentLatestMessengerBinding, LatestMessengerViewModel>() {
	override val viewModel: LatestMessengerViewModel by viewModels()
	
	override fun layoutId(): Int = R.layout.fragment_latest_messenger
	
	private var adapterMessenger: LatestMessageAdapter? = null
	private val latestMessageMap = HashMap<String, Message>()
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		// Khởi tạo adapter
		adapterMessenger = LatestMessageAdapter(requireContext())
		setUpRecyclerView()
		listenerForLatestMessages()
	}
	
	private fun setUpRecyclerView() {
		binding.recyclerView.apply {
			layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
			adapter = adapterMessenger
		}
		// Listener lamda function: onItemClick
		adapterMessenger?.onLastMessageClick = { user ->
			(activity as MainActivity).openFragment(
				ChattingMessageFragment.newInstance(user), R.id.mainContainer, REPLACE, true, null
			)
		}
		// set On click new message
		binding.btnNewMessage.setOnClickListener {
			(activity as MainActivity).openFragment(
				NewMessageFragment.newInstance(), R.id.mainContainer, REPLACE, true, null
			)
		}
		// on click button logout
		binding.btnLogOut.setOnClickListener {
			PreferenceHelper.getInstance().isSignIn = false
			PreferenceHelper.getInstance().userAccount = ""
			PreferenceHelper.getInstance().userPassword = ""
			(activity as MainActivity).openFragment(RegisterFragment.newInstance(), R.id.mainContainer, REPLACE, false, null)
		}
	}
	
	private fun listenerForLatestMessages() {
		val fromId = FirebaseAuth.getInstance().uid
		val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
		ref.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val chatMessage = snapshot.getValue(Message::class.java) ?: return
				latestMessageMap[snapshot.key!!] = chatMessage
				val listUser = mutableListOf<Message>()
				adapterMessenger?.clear()
				latestMessageMap.values.forEach {
					listUser.add(it)
				}
				adapterMessenger?.setList(listUser)
			}
			
			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
			}
			
			override fun onChildRemoved(snapshot: DataSnapshot) {
			}
			
			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
			}
			
			override fun onCancelled(error: DatabaseError) {
			}
			
		})
	}
	
	override fun onResume() {
		super.onResume()
		listenerForLatestMessages()
	}
	
	companion object {
		fun newInstance(): Fragment {
			val fragment = LatestMessengerFragment()
			fragment.arguments = Bundle()
			return fragment
		}
	}
}