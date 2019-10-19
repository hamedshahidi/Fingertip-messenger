/*
Name: Hamed Shahidi
Student# 1706225

<< TopChatter >> is an observer singleton that prints out the top 4 active chatters and the number of their messages
into console whenever number of active users is changed.

*/
import kotlin.math.min

object TopChatter : Observer {

    var userMsgCountMap = mutableMapOf<String, Int>()

    init {
        ChatHistory.registerObserver(this)
    }

    fun start() {
        ChatHistory.registerObserver(this, ChatHistory.observerListLoggedIn)
    }

    //  next checks username of received message and adds to its message count
    override fun newMessage(message: ChatMessage, privateMsg: Boolean) {
        if (!userMsgCountMap.containsKey(message.username)) {
            userMsgCountMap.put(message.username, 1)
        } else {
            userMsgCountMap.replace(message.username, userMsgCountMap.getValue(message.username) + 1)
        }
    }

    fun showTopChatters() {
        userMsgCountMap.remove("Server") //excluding server messages
        if (userMsgCountMap.isNotEmpty()) {
            var topChatters = " Top Chatter List:"
            userMsgCountMap.toList()
                    .sortedByDescending { it.second }
                    .subList(0, min(4, userMsgCountMap.toList().size))
                    .forEach { topChatters += "\n\r\t${it.first} ___ ${it.second} msg" }
            println(topChatters)
        } else println(" No active users.")
    }
}
