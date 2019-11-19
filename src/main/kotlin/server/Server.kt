package kotlin.server

import java.net.ServerSocket
import kotlin.concurrent.thread

class Server(val port: Int = 2222)
{
    var running = true

    fun handle(request: Request, response: Response)
    {
        println(request.resource)
        response.appende("<h1>")
        response.appende("Input: " + request.resource)
        response.appende("</h1>")
        response.send()
    }

    fun start()
    {
        val serverSocket = ServerSocket(port)
        while (true)
        {
            val socket = serverSocket.accept()

            thread {
                handle(Request(socket.getInputStream()), Response(socket.getOutputStream()))
            }
        }
    }
}

fun main() {
    println("Starting server")
    Server().start()
}