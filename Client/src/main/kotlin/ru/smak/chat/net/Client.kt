package ru.smak.chat.net

import ru.smak.chat.OutputLocation
import ru.smak.chat.net.Communicator
import java.net.Socket

class Client(host: String = "localhost", port: Int = 5104) {

    private var stop = false
    private val communicator = Communicator(Socket(host, port))

    private val gotServerDataListeners = mutableListOf<(String, OutputLocation)->Unit>()
    fun addGotServerDataListener(l: (String, OutputLocation)->Unit) = gotServerDataListeners.add(l)
    fun removeGotServerDataListener(l: (String, OutputLocation)->Unit) = gotServerDataListeners.remove(l)

    fun start() {
        communicator.doMainRoutine(::process)
    }

    fun stop() {
        stop = true
    }

    private fun process(data: String?) = data?.let{ d ->
        gotServerDataListeners.forEach {
            it(data, OutputLocation.USER_MESSAGE_LIST)
        }
    }

    fun send(data: String) = communicator.send(data)

}