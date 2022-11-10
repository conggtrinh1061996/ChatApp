package com.dev.beginprojectandroid.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.model.User
import com.dev.beginprojectandroid.databinding.ItemMessageBinding
import com.dev.beginprojectandroid.ui.adapter.ItemMessengerAdapter.MessengerViewHolder

class ItemMessengerAdapter(private val context: Context): RecyclerView.Adapter<MessengerViewHolder>() {
	private var users: MutableList<User> = mutableListOf()
	var onItemClick: ((user: User) -> Unit)? = null
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessengerViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding: ItemMessageBinding = DataBindingUtil.inflate(
			inflater,
			R.layout.item_message,
			parent, false
		)
		return MessengerViewHolder(binding)
	}
	
	override fun onBindViewHolder(holder: MessengerViewHolder, position: Int) {
		val user = users[position]
		holder.onBind(user)
		holder.itemView.setOnClickListener {
			onItemClick?.invoke(user)
		}
	}
	
	override fun getItemCount(): Int = users.size
	
	fun setListUser(list: List<User>) {
		users.addAll(list)
		notifyDataSetChanged()
	}
	
	class MessengerViewHolder(binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
		private val itemBinding = binding
		fun onBind(user: User) {
			itemBinding.userName.text = user.userName
		}
	}
}