package com.dev.beginprojectandroid.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.model.Message
import com.dev.beginprojectandroid.data.model.User
import com.dev.beginprojectandroid.databinding.ItemMessageBinding
import com.dev.beginprojectandroid.ui.adapter.LatestMessageAdapter.LastMessageViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LatestMessageAdapter(val context: Context): RecyclerView.Adapter<LastMessageViewHolder>() {
	private val messages : MutableList<Message> = mutableListOf()
	var onLastMessageClick: ((User) -> Unit)? = null
	var latestUser: User? = null
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastMessageViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding: ItemMessageBinding = DataBindingUtil.inflate(inflater, R.layout.item_message, parent, false)
		return LastMessageViewHolder(binding)
	}
	
	override fun onBindViewHolder(holder: LastMessageViewHolder, position: Int) {
		val latestMessage = messages[position]
		holder.onBind(latestMessage)
		//
		val chatPartnerId: String = if (latestMessage.fromId == FirebaseAuth.getInstance().uid) {
			latestMessage.toId
		} else latestMessage.fromId
		val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
		ref.addListenerForSingleValueEvent(object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				val user = snapshot.getValue(User::class.java)
				holder.itemBinding.userName.text = user?.userName
				holder.itemView.setOnClickListener {
					onLastMessageClick?.invoke(user!!)
				}
			}
			
			override fun onCancelled(error: DatabaseError) {
			}
			
		})
	}
	
	override fun getItemCount(): Int {
		return messages.size
	}
	
	class LastMessageViewHolder(binding: ItemMessageBinding): RecyclerView.ViewHolder(binding.root) {
		val itemBinding = binding
		fun onBind(message: Message) {
			itemBinding.contentMessage.text = message.contentMessage
			itemBinding.contentMessage.isVisible = true
		}
	}
	
	fun setList(latestMessages: List<Message>) {
		messages.addAll(latestMessages)
		notifyDataSetChanged()
	}
	
	fun clear() {
		messages.clear()
		notifyDataSetChanged()
	}
	
}