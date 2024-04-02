package ru.smak.chat

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.smak.chat.net.Client

class MainViewModel {
    val messages = mutableStateListOf("Пользователь1: Привет!", "Пользователь2: Тоже привет!")
    var serverMessage by mutableStateOf("Сообщение от Сервера")
    var userInput by mutableStateOf("")

    val scrollState = LazyListState(0)
    val sbAdapter = ScrollbarAdapter(scrollState)

    //private val c: Client = Client()

    fun send() {
        //c.send(userInput)
        messages.add(userInput)
        userInput = ""
    }

    init{
//        c.addGotServerDataListener {
//            messages.add(it)
//        }
    }

}
