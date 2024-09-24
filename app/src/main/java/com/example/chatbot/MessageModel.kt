package com.example.chatbot

import androidx.core.app.NotificationCompat.MessagingStyle.Message

data class MessageModel(
    val message :String,
    val role : String,
)
