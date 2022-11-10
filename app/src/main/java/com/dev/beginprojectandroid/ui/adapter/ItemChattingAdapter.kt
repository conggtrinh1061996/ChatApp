package com.dev.beginprojectandroid.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.dev.beginprojectandroid.R
import com.dev.beginprojectandroid.data.model.Message
import com.dev.beginprojectandroid.databinding.ItemChattingRowLeftBinding
import com.dev.beginprojectandroid.databinding.ItemChattingRowRightBinding
import com.dev.beginprojectandroid.utils.Constants.ViewTypeChatting.MESSAGE_LEFT
import com.dev.beginprojectandroid.utils.Constants.ViewTypeChatting.MESSAGE_RIGHT
import com.google.firebase.auth.FirebaseAuth

class ItemChattingAdapter(context: Context): RecyclerView.Adapter<ViewHolder>() {
	
	private val messages: MutableList<Message> = mutableListOf()
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		return when (viewType) {
			MESSAGE_LEFT -> {
				val inflater = LayoutInflater.from(parent.context)
				val binding = DataBindingUtil.inflate<ItemChattingRowLeftBinding>(inflater, R.layout.item_chatting_row_left, parent, false)
				ChattingLeftViewHolder(binding)
			}
			
			else -> {
				val inflater = LayoutInflater.from(parent.context)
				val binding = DataBindingUtil.inflate<ItemChattingRowRightBinding>(inflater, R.layout.item_chatting_row_right, parent, false)
				ChattingRightViewHolder(binding)
			}
		}
	}
	
	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		val message = messages[position]
		when (holder) {
			is ChattingLeftViewHolder -> {
				holder.onBind(message)
			}
			
			is ChattingRightViewHolder -> {
				holder.onBind(message)
			}
		}
	}
	
	override fun getItemCount(): Int = messages.size
	
	override fun getItemViewType(position: Int): Int {
		val fromId = messages[position].fromId
		return if (fromId == FirebaseAuth.getInstance().uid) MESSAGE_RIGHT else MESSAGE_LEFT
	}
	
	class ChattingLeftViewHolder(binding: ItemChattingRowLeftBinding) : ViewHolder(binding.root) {
		private val itemBinding = binding
		fun onBind(message: Message) {
			itemBinding.txtChatting.text = message.contentMessage
		}
	}
	
	class ChattingRightViewHolder(binding: ItemChattingRowRightBinding) : ViewHolder(binding.root) {
		private val itemBinding = binding
		fun onBind(message: Message) {
			itemBinding.txtChatting.text = message.contentMessage
		}
	}
	
	fun setListMessage(list: List<Message>) {
		messages.addAll(list)
		notifyDataSetChanged()
	}
	
	fun add(message: Message) {
		messages.add(message)
		notifyItemInserted(messages.indexOf(message))
	}
}