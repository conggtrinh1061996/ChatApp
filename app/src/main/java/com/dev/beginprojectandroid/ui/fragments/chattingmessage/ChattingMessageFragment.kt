package com.dev.beginprojectandroid.ui.fragments.chattingmessage

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.model.Message
import com.dev.beginprojectandroid.data.model.User
import com.dev.beginprojectandroid.databinding.FragmentChattingMessageBinding
import com.dev.beginprojectandroid.ui.adapter.ItemChattingAdapter
import com.dev.beginprojectandroid.ui.base.BaseFragment
import com.dev.beginprojectandroid.utils.Constants.USER_NAME_KEY
import com.dev.beginprojectandroid.utils.Constants.USER_UID_KEY
import com.dev.beginprojectandroid.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class ChattingMessageFragment : BaseFragment<FragmentChattingMessageBinding, ChattingMessageViewModel>() {
	override val viewModel: ChattingMessageViewModel by viewModels()
	
	override fun layoutId(): Int = R.layout.fragment_chatting_message
	
	private var titleUserName: String? = null
	private var userUId: String? = null
	private var adapterChatting: ItemChattingAdapter? = null
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		setUp()
		setUpObserver()
		setUpAdapter()
		// listener message
		listenForMessage()
		// setUp click send button
		binding.btnSend.setOnClickListener {
			performSendMessage()
		}
	}
	
	private fun setUp() {
		// kiểm tra sự thay đổi text với editText
		binding.edtTextChatting.addTextChangedListener { text ->
			showOrHideButtonSend(text!!.isNotEmpty())
		}
		// Lấy data được gửi từ Bundle
		arguments?.let {
			titleUserName = it.getString(USER_NAME_KEY)
			userUId = it.getString(USER_UID_KEY)
		}
		// click back
		binding.btnBack.setOnClickListener {
			parentFragmentManager.popBackStack()
		}
		
	}
	
	private fun setUpObserver() {
		// Set title user name
		viewModel.getUserName(titleUserName!!)
		viewModel.textUserName.observe(viewLifecycleOwner) {
			binding.titleNameUser.text = it
		}
	}
	
	private fun setUpAdapter() {
		adapterChatting = ItemChattingAdapter(requireContext())
		binding.recyclerViewChatting.adapter = adapterChatting
	}
	
	/**
	 * Thuc thi gui tin nhan
	 */
	private fun performSendMessage() {
		// Send message to firebase
		val fromId = FirebaseAuth.getInstance().uid
		val toId = userUId
		if (fromId == null || toId == null) return
		val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
		val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
		val chatMessage = Message(reference.key!!, binding.edtTextChatting.text.toString(), fromId, toId, System.currentTimeMillis())
		reference.setValue(chatMessage)
			.addOnSuccessListener {
				Logger.d("Saved my chatting: $chatMessage")
				binding.edtTextChatting.setText(requireContext().getString(R.string.empty))
			}
		
		toReference.setValue(chatMessage)
			.addOnSuccessListener {
				binding.edtTextChatting.setText(requireContext().getString(R.string.empty))
			}
		// latest message update
		val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
		latestMessageRef.setValue(chatMessage)
			.addOnSuccessListener {
			
			}
		// Scroll Recycler view to the end
		binding.recyclerViewChatting.smoothScrollToPosition(adapterChatting?.itemCount!!)
	}
	
	private fun listenForMessage() {
		val fromId = FirebaseAuth.getInstance().uid
		val toId = userUId
		val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")
		
		ref.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				val chatMessage = snapshot.getValue(Message::class.java)
				if (chatMessage != null) {
					adapterChatting!!.add(chatMessage)
					binding.recyclerViewChatting.scrollToPosition(adapterChatting?.itemCount!!)
				}
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
	
	private fun showOrHideButtonSend(isShow: Boolean) {
		binding.btnSend.isVisible = isShow
	}
	
	companion object {
		fun newInstance(user: User): Fragment {
			val fragment = ChattingMessageFragment()
			val args = Bundle()
			args.putString(USER_NAME_KEY, user.userName)
			args.putString(USER_UID_KEY, user.uid)
			fragment.arguments = args
			return fragment
		}
	}
}