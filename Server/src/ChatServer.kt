/*
Name: Hamed Shahidi
Student# 1706225

<< ChatServer >> is a class used for initiating server socket, that listens to incoming connestion requests. It generates threads
and inside each thread it accpets the incoming server socket connection request and creates instance of CommandInterpreter for that connection.
Threads makes it possible to run processes with blocking calls ( accept() in here) simultaneously.
*/
import java.io.PrintStream
import java.net.ServerSocket

class ChatServer {

    fun serve() {
        ChatConsole.start()
        TopChatter.start()
        try {
            val serverSocket = ServerSocket(0)

            println("Port number: ${serverSocket.localPort}")

            while (true) {
                val ss = serverSocket.accept()
                println("New connection ${ss.inetAddress.hostAddress} ${ss.port}")

                val printer = PrintStream(ss.getOutputStream(), true)

                val myThread = Thread(CommandInterpreter(ss.getInputStream(), ss.getOutputStream()))
                myThread.start()

                printer.println("")
            }
        } catch (e: Exception) {
            println("Exception: ${e.message}")
        } finally {
            println("Done.")
        }
    }
}