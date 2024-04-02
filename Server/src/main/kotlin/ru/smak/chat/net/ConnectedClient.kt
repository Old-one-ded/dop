package ru.smak.chat.net

import ru.smak.chat.net.Communicator
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class ConnectedClient(socket: Socket) {

    companion object{
        val clients = mutableListOf<ConnectedClient>()
    }

    private val communicator = Communicator(socket)

    val isAlive: Boolean
        get() = !communicator.isStopped

    init{
        clients.add(this)
        communicator.addOnStopListener {
            clients.remove(this)
        }
        communicator.doMainRoutine(::process)
    }

    private fun process(data: String) = sendToAll(data)

    private fun sendToAll(data: String) = clients.apply {
        removeIf { !it.isAlive }
        forEach {
            it.communicator.send(data)
        }
    }

    fun stop() = communicator.stop()

}