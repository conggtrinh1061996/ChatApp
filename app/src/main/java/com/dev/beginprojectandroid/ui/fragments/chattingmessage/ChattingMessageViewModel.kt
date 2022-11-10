package com.dev.beginprojectandroid.ui.fragments.chattingmessage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dev.beginprojectandroid.ui.base.BaseViewModel

class ChattingMessageViewModel: BaseViewModel<ChattingMessageNavigator>() {
	private val _textUserName = MutableLiveData<String>()
	val textUserName: LiveData<String>
			get() = _textUserName
	
	fun getUserName(userName: String) {
		_textUserName.value = userName
	}
}