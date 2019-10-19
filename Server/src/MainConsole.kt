/*
Name: Hamed Shahidi
Student# 1706225

<< MainConsole >> is used for debugging. It runs the chat server code nly on console without creating/accepting any server socket.
*/
fun main(args: Array<String>) {

    val ci = CommandInterpreter(System.`in`, System.out)
    ci.run()
}