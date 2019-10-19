/*
Name: Hamed Shahidi
Student# 1706225

<< ChatConsole >> is class registered as an observer and prints out to System.out all chat messages in the conversation.
*/
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ChatConsole : Observer {

    fun start() {
        ChatHistory.registerObserver(this, ChatHistory.observerListLoggedIn)
    }

    override fun newMessage(message: ChatMessage, privateMsg: Boolean) {
        when (message.username) {
            "Server" -> System.out.println("\r " +
                    "-${LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"))}-" +
                    " ${message.message}")
            else -> {
                when (privateMsg) {
                    false -> {

                        /*if (TopChatter.userMsgCountMap.isNotEmpty()){
                            ChatHistory.notifyObservers(
                                    ChatMessage(TopChatter.showTopChatters(), "TopChatter", LocalDateTime.now()),
                                    ChatHistory.observerListLoggedIn,
                                    false)

                        }*/


                        System.out.println("\r " +
                                "-${message.time.format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"))}-" +
                                " ${message.username} said: ${message.message}")
                    }
                }
            }
        }
    }
}
