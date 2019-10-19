package HamedShahidi.fingertipmessenger

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cell_layout_onlineusers.view.*
import kotlinx.android.synthetic.main.cell_layout_server.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val VIEW_TYPE_MESSAGE_SERVER = 3

class UserAdapter(val context: Context) : RecyclerView.Adapter<UserMsgViewHolder>() {

    private val userList = ArrayList<ChatMessage>()

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        val msg = userList[position]
        Log.d("UserAdapter", "getItemViewType: ${msg.name}")
        return if (msg.name == "Online users#") {
            VIEW_TYPE_MESSAGE_SERVER
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: UserMsgViewHolder, position: Int) {
        val msg = userList[position]
        holder?.bind(msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMsgViewHolder {
        return MsgServerViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_layout_onlineusers, parent, false))
    }

    fun addUserList(msg: ChatMessage) {
        if (!msg.message.isNullOrBlank()) {
            userList.clear()
            val onlineUsers = getListOfOnlineUsers(msg.message)
            onlineUsers.forEach { username ->
                userList.add(ChatMessage(username, "time", "name"))
            }
        }
        notifyDataSetChanged()
    }

    private fun getListOfOnlineUsers(message: String): List<String> {
        return message
                .substringAfter(':')
                .trimStart('/')
                .split('/')
    }

    inner class MsgServerViewHolder(view: View) : UserMsgViewHolder(view) {
        private var username = view.tv_username

        override fun bind(msg: ChatMessage) {
            username.text = msg.message
        }
    }
}

open class UserMsgViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(msg: ChatMessage) {}
}

