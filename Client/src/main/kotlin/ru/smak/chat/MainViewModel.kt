package ru.smak.chat

import androidx.compose.foundation.ScrollbarAdapter
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import ru.smak.chat.net.Client

class MainViewModel {
    val messages = mutableStateListOf<String>()
    var serverMessage by mutableStateOf("")
    var userInput by mutableStateOf("")

    val scrollState = LazyListState(0)
    val sbAdapter = ScrollbarAdapter(scrollState)

    private val c: Client = Client()

    fun send() {
        c.send(userInput)
        userInput = ""
    }

    init{
        c.addGotServerDataListener { msg, loc ->
            when(loc){
                OutputLocation.USER_MESSAGE_LIST -> messages.add(msg)
                OutputLocation.SYSTEM_TEXT -> serverMessage = msg
            }

        }
        c.start()
    }

}
