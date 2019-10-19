/*
Name: Hamed Shahidi
Student# 1706225

<< Observable >> is a custom interface used for observable classes, used for loosely coupling.
login() logout() methods added. notify() function also gets the receiver observers list in a set<> collection and
private message trigger, as parameters.
*/
interface Observable {

    fun registerObserver(observer: Observer, list: MutableSet<Observer> = ChatHistory.observerList)
    fun deregisterObserver(observer: Observer, list: MutableSet<Observer> = ChatHistory.observerList)
    fun notifyObservers(message: ChatMessage, observers: MutableSet<Observer>, privateMsg: Boolean)
    fun login(username: String, observer: Observer)
    fun logout(username: String, observer: Observer)
}