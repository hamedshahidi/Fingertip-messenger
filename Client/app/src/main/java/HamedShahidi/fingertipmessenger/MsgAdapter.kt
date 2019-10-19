package HamedShahidi.fingertipmessenger

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.cell_layout_in.view.*
import kotlinx.android.synthetic.main.cell_layout_out.view.*
import kotlinx.android.synthetic.main.cell_layout_server.view.*
import java.time.format.DateTimeFormatter

private const val VIEW_TYPE_MESSAGE_OUT = 1
private const val VIEW_TYPE_MESSAGE_IN = 2
private const val VIEW_TYPE_MESSAGE_SERVER = 3

class MsgAdapter(val context: Context) : RecyclerView.Adapter<ChatMsgViewHolder>() {

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    private val msgList = ArrayList<ChatMessage>()

    override fun getItemCount(): Int {
        return msgList.size
    }

    override fun getItemViewType(position: Int): Int {
        val msg = msgList[position]
        return when {
            msg.name == CurrentUser.user -> VIEW_TYPE_MESSAGE_OUT
            msg.name == "Server" -> VIEW_TYPE_MESSAGE_SERVER
            else -> VIEW_TYPE_MESSAGE_IN
        }
    }

    override fun onBindViewHolder(holder: ChatMsgViewHolder, position: Int) {
        val msg = msgList[position]
        holder?.bind(msg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMsgViewHolder {
        return when (viewType) {
            VIEW_TYPE_MESSAGE_OUT -> MsgOutViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_layout_out, parent, false))
            VIEW_TYPE_MESSAGE_SERVER -> MsgServerViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_layout_server, parent, false))
            else -> MsgInViewHolder(LayoutInflater.from(context).inflate(R.layout.cell_layout_in, parent, false))
        }
    }

    fun addMessage(msg: ChatMessage) {
        msgList.add(msg)
        notifyDataSetChanged()
    }

    inner class MsgOutViewHolder(view: View) : ChatMsgViewHolder(view) {
        var msgMessage = view.tv_msg_out
        var msgTime = view.tv_msg_out_time

        override fun bind(msg: ChatMessage) {
            msgMessage.text = msg.message
            msgTime.text = msg.time.format(formatter)
        }
    }

    inner class MsgInViewHolder(view: View) : ChatMsgViewHolder(view) {
        private var msgMessage = view.tv_msg_in
        private var msgTime = view.tv_msg_in_time
        private var msgUser = view.tv_msg_in_username

        override fun bind(msg: ChatMessage) {
            msgMessage.text = msg.message
            msgTime.text = msg.time.format(formatter)
            msgUser.text = msg.name
        }
    }

    inner class MsgServerViewHolder(view: View) : ChatMsgViewHolder(view) {
        private var msgMessage = view.tv_server_msg
        private var msgTime = view.tv_server_time

        override fun bind(msg: ChatMessage) {
            msgMessage.text = msg.message
            msgTime.text = msg.time.format(formatter)
        }
    }
}

open class ChatMsgViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(msg: ChatMessage) {}
}