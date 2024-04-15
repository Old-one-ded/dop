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
    private var name: String? = null

    val isAlive: Boolean
        get() = !communicator.isStopped

    init{
        clients.add(this)
        communicator.addOnStopListener {
            clients.remove(this)
        }
        communicator.doMainRoutine(::process)
        send(Command.SYSTEM_MESSAGE, "Введите своё имя:")
    }

    private fun send(cmd: Command, data: String) =
        communicator.send("$cmd:$data")

    private fun process(data: String){
        name?.let { cName ->
            sendToAll("$name: $data")
        } ?: run {
            if (clients.firstOrNull { it.name?.uppercase() == data.uppercase() } != null)
                send(Command.SYSTEM_MESSAGE,"Выбранное имя занято. Введите другое:")
            else {
                if (!data.matches(Regex("^[A-ZА-Я][\\w_-]+$", RegexOption.IGNORE_CASE)))
                    send(Command.SYSTEM_MESSAGE, "Имя должно содержать только буквы и цифры")
                else {
                    name = data
                    send(Command.SYSTEM_MESSAGE, "Введите своё сообщение:")
                    sendToAll("К нам присоединился $name!")
                }
            }
        }
    }

    private fun sendToAll(data: String) = clients.apply {
        removeIf { !it.isAlive }
        forEach {
            it.send(Command.USER_MESSAGE, data)
        }
    }

    fun stop() = communicator.stop()

}