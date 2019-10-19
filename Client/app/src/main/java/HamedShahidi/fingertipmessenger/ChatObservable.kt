package HamedShahidi.fingertipmessenger

interface ChatObservable {

    fun registerObserver(observer: ChatObserver)
    fun deregisterObserver(observer: ChatObserver)
    fun notifyObserver(msg: ChatMessage)
}