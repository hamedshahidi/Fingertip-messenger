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

    fun showTopChatters(): String {
        userMsgCountMap.remove("Server") //excluding server messages
        var topChatters = "Top Chatter List:"
        if (userMsgCountMap.isNotEmpty()) {
            userMsgCountMap.toList()
                    .sortedByDescending { it.second }
                    //.subList(0, min(5, userMsgCountMap.toList().size))
                    .forEach { topChatters += " ${it.first}-${it.second}" }
            println(topChatters)
        } else topChatters = ":No active users"
        return topChatters
    }
}
