package ru.smak.chat.net

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class Communicator(private val socket: Socket) {

    private val onStopListeners = mutableListOf<()->Unit>()
    fun addOnStopListener(l: ()->Unit) = onStopListeners.add(l)
    fun removeOnStopListener(l: ()->Unit) = onStopListeners.remove(l)

    private val pw = PrintWriter(socket.getOutputStream())
    private val br = BufferedReader(InputStreamReader(socket.getInputStream()))
    var isStopped = true
        private set

    fun send(data: String) = pw.apply {
        try {
            println(data)
            flush()
        } catch (_: Throwable){
            stop()
        }
    }

    fun doMainRoutine(onDataReceived: (String)->Unit){
        isStopped = false
        thread {
            try {
                while (!isStopped) {
                    val data = br.readLine()
                    onDataReceived(data)
                }
            } catch (_: Throwable){
            } finally {
                stop()
            }
        }
    }

    fun stop(){
        if (!isStopped) {
            isStopped = true
            socket.close()
            onStopListeners.forEach { it() }
        }
    }
}