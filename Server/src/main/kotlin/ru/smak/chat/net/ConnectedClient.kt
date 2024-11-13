package ru.smak.chat.net

import java.net.Socket

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
        communicator.doMainRoutine(::send)
    }

    private fun send(data: String) =
        communicator.send(addMatrices(matrix1,matrix2))

    private val matrix1 = listOf(
        listOf(5, 12, 7),
        listOf(14, 3, 6),
        listOf(9, 8, 10)
    )

    private val matrix2 = listOf(
        listOf(2, 15, 9),
        listOf(11, 4, 13),
        listOf(6, 1, 8)
    )
    fun addMatrices(a: List<List<Int>>, b: List<List<Int>>): String {
        val result = mutableListOf<MutableList<Int>>()

        // Сложение матриц
        for (i in a.indices) {
            val row = mutableListOf<Int>()
            for (j in a[i].indices) {
                row.add(a[i][j] + b[i][j])
            }
            result.add(row)
        }

        // Определение максимальной длины числа для форматирования
        val maxLength = result.flatten().maxOf { it.toString().length }

        // Построение строки с результатом сложения
        return result.joinToString("\n") { row ->
            row.joinToString(" ") { it.toString().padStart(maxLength) }
        } + "\n"
    }

    fun stop() = communicator.stop()

}