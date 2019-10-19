/*
Name: Hamed Shahidi
Student# 1706225

<< ChatHistory >> is an observable singleton used for storing all messages and notifying observers about new messages.
*/
import java.time.format.DateTimeFormatter
import kotlin.math.min

object ChatHistory : Observable {

    private val messageHistory = mutableListOf<ChatMessage>()
    val observerList = mutableSetOf<Observer>()
    val observerListLoggedIn = mutableSetOf<Observer>()
    val usernameObserverTable = mutableMapOf<String, Observer>()

    override fun registerObserver(observer: Observer, list: MutableSet<Observer>) {
        list.add(observer)
    }

    override fun deregisterObserver(observer: Observer, list: MutableSet<Observer>) {
        list.remove(observer)
    }

    override fun notifyObservers(message: ChatMessage, observers: MutableSet<Observer>, privateMsg: Boolean) {
        observers.forEach { it.newMessage(message, privateMsg) }
    }

    override fun login(username: String, observer: Observer) {
        usernameObserverTable.put(username, observer)
    }

    override fun logout(username: String, observer: Observer) {
        //usernameObserverTable.remove(username, observer)
        observerListLoggedIn.remove(observer)
    }

    fun addMessage(msg: ChatMessage): Unit {
        messageHistory.add(msg)
    }

    override fun toString(): String {
        var historyString = " >> History:"
        messageHistory.takeLast(min(20, messageHistory.size)).forEach { c: ChatMessage ->
            historyString += "\n\r >>\t -${c.time.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"))}- ${c.username} said: ${c.message} "
        }
        if (historyString == " >> History:" ) historyString = " >> Chat history is Empty!"
        return historyString
    }
}