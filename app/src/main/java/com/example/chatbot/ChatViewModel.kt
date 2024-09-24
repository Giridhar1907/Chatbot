package com.example.chatbot
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerationConfig
import com.google.ai.client.generativeai.type.RequestOptions
import com.google.ai.client.generativeai.type.content
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    val messageList by lazy {
        mutableStateListOf<MessageModel>()
    }
    private var apikey = "AIzaSyB_6qQLzZMcmKnIsjR2F6wuVaPO_Z0XWMM"
    private var model = "gemini-1.5-flash-001"
    val generativeModel = GenerativeModel(
        modelName = model,
        apiKey = apikey,
        generationConfig = generationConfig {
            temperature = 0.15f
            topK = 32
            topP = 1f
            maxOutputTokens = 4096
        }
    )

    fun sendMessage(question : String){
        viewModelScope.launch {
            try{
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) { text(it.message) }
                    }.toList()
                )
                messageList.add(MessageModel(question,"user"))
                messageList.add(MessageModel("Typing...","model"))
                val response = chat.sendMessage(question)
                messageList.removeLast()
                messageList.add(MessageModel(response.text.toString(),"model"))
            }catch (e: Exception){
                messageList.removeLast()
                messageList.add(MessageModel("Error : "+e.message.toString(),"model"))
            }


        }

    }
}