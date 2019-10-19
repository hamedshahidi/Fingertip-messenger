/*
Name: Hamed Shahidi
Student# 1706225

<< Observer >> is a custom interface used for observer classes, used for loosely coupling.
*/
interface Observer {

    fun newMessage(message: ChatMessage, privateMsg: Boolean = false)
}