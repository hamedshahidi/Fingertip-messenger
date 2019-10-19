/*
Name: Hamed Shahidi
Student# 1706225

<< Users >> is a an observable singleton responsible used for adding/removing unique usernames and keeping track of them
*/
object Users : Observable {

    val usernameList = mutableSetOf<String>()

    override fun registerObserver(observer: Observer, list: MutableSet<Observer>) {
        list.add(observer)
    }

    override fun notifyObservers(message: ChatMessage, observers: MutableSet<Observer>, privateMsg: Boolean) {
        observers.forEach { it.newMessage(message, privateMsg) }
    }

    fun addUser(username: String): Boolean {
        return if (usernameList.contains(username)) {
            println(" A try to use existing username -> \"$username\"")
            false
        } else {
            usernameList.add(username)
            println(" New user logged in ->  + $username")
            TopChatter.userMsgCountMap.put(username, 0)
            TopChatter.showTopChatters()
            true
        }
    }

    fun removeUser(observer: CommandInterpreter): Unit {
        println(" User logged out ->  - ${observer.username}")
        usernameList.remove(observer.username)
        TopChatter.userMsgCountMap.remove(observer.username)
        TopChatter.showTopChatters()
    }

    fun toFormattedString(): String {
        var string = " >> Logged in users:"
        usernameList.forEach { string += "\n\r >> \t\t${usernameList.indexOf(it) + 1}.$it" }
        return string
    }

    override fun login(username: String, observer: Observer) {}
    override fun logout(username: String, observer: Observer) {}
    override fun deregisterObserver(observer: Observer, list: MutableSet<Observer>) {}
}