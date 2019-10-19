/*
Name: Hamed Shahidi
Student# 1706225
<< CommandInterpreter >> is an observer class that recognizes the specified commands (
    :? - shows help.
    :user <username>- creates a new username. E.g. :user john
    :login - logs in to chat room if username already exists.
    :logout - logs out of chat room, keeps the connection.
    :quit - closes connection to chat server.
    :messages - shows last 20 messages in the conversation.
    :users - shows active users.
    @<char(s)> <message>- sends private message/whisper. E.g. "@j hello" sends "hello" to username staring with "j"
*/
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintStream
import java.time.LocalDateTime
import java.util.*
import kotlin.math.min
import java.time.format.DateTimeFormatter

class CommandInterpreter(input: InputStream, output: OutputStream) : Runnable, Observer {

    val scanner: Scanner = Scanner(input)
    val printer: PrintStream = PrintStream(output)
    var quit = false
    var loggedIn = false
    var username: String = ""
    val formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm")

    init {
        ChatHistory.registerObserver(this)
    }

    // checks the type of message server/private/public and acts based on that
    override fun newMessage(message: ChatMessage, privateMsg: Boolean) {
        when (message.username) {"Server" -> printer.println("${"\u001b[47m\u001b[30m"
                + message.time.format(formatter)} ${"\u001b[0m\u001B[33;1m " + message.message}\u001B[0m")
            else -> {
                when (privateMsg) {
                    false -> printer.println("${"\u001b[47m\u001b[30m" + message.time.format(formatter)} " +
                            "${"\u001b[0m\u001b[36;1m " + message.username}${"\u001b[0m: " + message.message}"
                    )
                    true -> printer.println("${"\u001b[47m\u001b[30m" + message.time.format(formatter)} " +
                            "${"\u001b[0m\u001b[35;1m " + message.username}${" [whisper]: \u001B[30;1m"
                                    + message.message}\u001B[0m")
                }
            }
        }
    }

    override fun run() {
        printer.println("${"\u001b[47m\u001b[30m" + LocalDateTime.now().format(formatter)}\u001B" +
                "[34;1m Welcome to 2018 chat server.\u001B[0m")
        while (!quit) {
            val userInput = scanner.nextLine()
            if (userInput.isNotEmpty()) {
                if (username.isEmpty()) {
                    when (userInput[0]) {
                        ':' -> {
                            when (userInput.substringBefore(' ')) {
                                ":quit" -> quitUser()
                                ":?" -> showHelp()
                                else -> {
                                    when (userInput.substring(0, min(6, userInput.lastIndex))) {
                                        ":user " -> {
                                            if (userInput.substringAfter(' ').isNotBlank()) {
                                                username = userInput.substring(6, userInput.length)
                                                if (Users.addUser(username)) { // addUser returns true is succeeds
                                                    printer.println(" >> Username is set to \"$username\"")
                                                    ChatHistory.registerObserver(this, ChatHistory.observerListLoggedIn) //observer added to logged in list
                                                    ChatHistory.login(username, this) // maps observer and username of it
                                                    ChatHistory.notifyObservers(
                                                            ChatMessage("<< $username >> joined the room.", "Server", LocalDateTime.now())
                                                            , ChatHistory.observerListLoggedIn.filter { it != this }.toMutableSet() // notify to other observers only
                                                            , false
                                                    )
                                                    printer.println("${"\u001b[47m\u001b[30m" + LocalDateTime.now().format(formatter)}\u001B" +
                                                            "[34;1m You are logged in to chat room.\u001B[0m")
                                                    loggedIn = true
                                                } else {
                                                    username = ""
                                                    printer.println(" >> Try another username, \"${userInput.substringAfter(' ')}\" already exists." +
                                                            "\n\r >> Use \":?\" for help.")
                                                }
                                            } else {
                                                printer.println(" >> Blank username is not accepted.\n\r >> Please provide an alphabetic or alphanumeric username.")
                                                printer.println(" >> Use \":?\" for help.")
                                            }
                                        }
                                        else -> {
                                            printer.println(" >> User name is not set! Use \":?\" for help.")
                                        }
                                    }
                                }
                            }

                        }
                        else -> printer.println(" >> Please set your username first. Use \":?\" for help.")
                    }
                } else { // here user name exists
                    if (loggedIn) {
                        when (userInput[0]) {
                            ':' -> {
                                when (userInput.substringBefore(' ')) {
                                    ":user" -> printer.println(" >> You already have username \"$username\"")
                                    ":users" -> printer.println(Users.toFormattedString())
                                    ":messages" -> printer.println(ChatHistory)
                                    ":login" -> loginUser() // maps observer and its username
                                    ":logout" -> {
                                        ChatHistory.logout(username, this) // removes (observer,username) pair from the map
                                        ChatHistory.notifyObservers(
                                                ChatMessage("<< $username >> Left the room.", "Server", LocalDateTime.now())
                                                , ChatHistory.observerListLoggedIn.filter { it != this }.toMutableSet()
                                                , false
                                        )
                                        Users.removeUser(this) //removes username from active username list
                                        printer.println(
                                                "${"\u001b[47m\u001b[30m" + LocalDateTime.now().format(formatter)}\u001B" +
                                                        "[34;1m You are logged out of chat room.\u001B[0m")
                                        loggedIn = false
                                    }
                                    ":?" -> showHelp()
                                    ":quit" -> quitUser() //removes username and observer from all list and closes connection
                                    else -> printer.println(" >> Didn't get it $userInput.")
                                }
                            }
                            '@' -> { //private messages/whisper
                                //first reads everything typed after "@"
                                val searchParameter: String = userInput.substringBefore(' ').drop(1)
                                var matchList = Users.usernameList
                                        .filter { it != username } //excludes own username from the list
                                        .toMutableSet()
                                        //next compares user names and search parameter
                                        .filter { it.regionMatches(0, searchParameter, 0, searchParameter.length) }
                                if (Users.usernameList.contains(searchParameter)) { //looking for exact match in user names
                                    matchList = Users.usernameList.toMutableList().minus(Users.usernameList.toMutableList()).toMutableList()
                                    matchList.add(searchParameter)
                                }

                                when {
                                    matchList.size == 0 -> printer.println(" >> No user found starting with \"$searchParameter\".")
                                    matchList.size > 1 -> {
                                        printer.println(" >> ${matchList.size} users found starting with \"$searchParameter\":")
                                        matchList.forEach { printer.println("\r\t-> $it") }
                                    }
                                    matchList[0] == username -> printer.println(" >> You can not whisper to yourself :p")
                                    else -> { //sending whisper/private message
                                        val whisper: ChatMessage = ChatMessage(userInput.substringAfter(' '), username, LocalDateTime.now())
                                        val privateReceiver = mutableSetOf<Observer>()
                                        privateReceiver.add(ChatHistory.usernameObserverTable.getValue(matchList[0])) //setting receiver
                                        ChatHistory.notifyObservers(whisper, privateReceiver, true) //sending message as private
                                    }
                                }
                            }
                            else -> { //normal message
                                val message: ChatMessage = ChatMessage(userInput, username, LocalDateTime.now())
                                val publicReceiver = ChatHistory.observerListLoggedIn.filter { it != this }.toMutableSet() //excluding itself from receivers
                                ChatHistory.addMessage(message)
                                ChatHistory.notifyObservers(message, publicReceiver, false)
                            }
                        }
                    } else { //here user is logged out of chat room
                        when (userInput[0]) {
                            ':' -> {
                                when (userInput.substringBefore(' ')) {
                                    ":login" -> loginUser()
                                    ":quit" -> quitUser()
                                    else -> printer.println(" >> Didn't get it $userInput.")
                                }
                            }
                            else -> printer.println(" >> Use commands \n\r >>\t" +
                                    ":login ........ to log back in to chat room.\n\r >>\t" +
                                    ":quit ......... to close connection to chat server.")
                        }
                    }
                }
            } else {
                // here user input is empty
                if (username.isEmpty()) {
                    printer.println(" >> User name is not set. Use \":?\" for help.")
                }
            }
        }
    }

    private fun showHelp() {
        printer.println("\r >>\t" +
                ":user <username> ........ Creates a new username. E.g. :user john\n\r >>\t" +
                ":login ...................Logs in to chat room if username already exists.\n\r >>\t" +
                ":logout ................. Logs out of chat room, keeps the connection.\n\r >>\t" +
                ":quit ................... Closes connection to chat server.\n\r >>\t" +
                ":messages ............... Shows last 20 messages in the conversation.\n\r >>\t" +
                ":users .................. Shows active users.\n\r >>\t" +
                "@<char(s)> <message> .... Sends private message/whisper.\n\r >>\t " +
                "                          E.g. \"@j hello\" sends \"hello\" to username staring with \"j\"\n\r >>\t" +
                ":?....................... Help")
    }

    private fun loginUser() {
        if (ChatHistory.usernameObserverTable.containsValue(this)) { //searches for existing username for this observer
            ChatHistory.registerObserver(this, ChatHistory.observerListLoggedIn)
            username = ChatHistory.usernameObserverTable.filterValues { it == this }.keys.elementAt(0).toString() //gets username
            Users.addUser(username) //adds username back to active users list
            println("User logged back in ->   ${username}")
            ChatHistory.notifyObservers(
                    ChatMessage("<< $username >> joined the room.", "Server", LocalDateTime.now())
                    , ChatHistory.observerListLoggedIn.filter { it != this }.toMutableSet()
                    , false
            )
            printer.println("${"\u001b[47m\u001b[30m" + LocalDateTime.now().format(formatter)}\u001B[34;1m You are logged in to chat room.\u001B[0m")
            loggedIn = true
        }
    }

    private fun quitUser() {
        ChatHistory.logout(username, this)
        ChatHistory.usernameObserverTable.remove(username, this)
        if (loggedIn) {
            ChatHistory.notifyObservers(
                    ChatMessage("<< $username >> Left the room.", "Server", LocalDateTime.now())
                    , ChatHistory.observerListLoggedIn.filter { it != this }.toMutableSet()
                    , false
            )
        }
        Users.removeUser(this)
        printer.println(" >> Goodbye.")
        scanner.close()
        loggedIn = false
        quit = true
    }
}