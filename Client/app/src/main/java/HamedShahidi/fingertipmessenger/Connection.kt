package HamedShahidi.fingertipmessenger


import android.content.SharedPreferences
import java.io.IOException
import java.io.InputStream
import java.io.PrintStream
import java.net.Socket
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

object Connection : ChatObservable {

    lateinit var socket: Socket
    lateinit var input: InputStream
    lateinit var output: PrintStream
    lateinit var preferences: SharedPreferences
    var connected = false
    val msgReceiverActivitySet = mutableSetOf<ChatObserver>()

    override fun registerObserver(observer: ChatObserver) {
        msgReceiverActivitySet.add(observer)
    }

    override fun deregisterObserver(observer: ChatObserver) {
        msgReceiverActivitySet.remove(observer)
    }

    override fun notifyObserver(msg: ChatMessage) {
        msgReceiverActivitySet.forEach { it.updateMessage(msg) }
    }

    fun connect() {

        try {
            val ip = preferences.getString("ip", "10.0.2.2")
            val port = 55555

            socket = Socket(ip, port)

            do {
                if (socket.isConnected) connected = true
            } while (!connected)


            input = socket.getInputStream()
            output = PrintStream(socket.getOutputStream(), true)

            while (true) {
                val scanner = Scanner(input)
                val line = scanner.nextLine()
                notifyObserver(createMessage(line))
            }

        } catch (e: IOException) {
            println("Exception: ${e.message}")
        } finally {
            println("Done.")
        }
    }

    fun writeToServer(msg: ChatMessage) {
        thread { output.println(msg.message) }
    }

    private fun createMessage(string: String): ChatMessage {

        val name = string.substringBefore(':')
        val msgBody = string.substringAfter(']').trimStart(' ')
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))

        return ChatMessage(msgBody, time, name)
    }

    fun addPref(pref: SharedPreferences) {
        preferences = pref
    }
}
