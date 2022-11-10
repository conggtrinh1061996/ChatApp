package com.dev.beginprojectandroid.data.model

data class Message (
	var id: String = "",
	var contentMessage: String = "",
	var fromId: String = "",
	var toId: String = "",
	var timeStamp: Long = 0
)