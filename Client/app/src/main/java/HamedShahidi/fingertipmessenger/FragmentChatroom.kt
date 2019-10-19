package HamedShahidi.fingertipmessenger

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

import java.time.LocalDateTime

class FragmentChatroom : Fragment(), ChatObserver {

    var title: String = ""

    lateinit var msgAdapter: MsgAdapter

    override fun updateMessage(msg: ChatMessage) {
        val view = view
        if (view != null && msg.name != "Online users#") {
            activity?.runOnUiThread {
                msgAdapter.addMessage(msg)
                view.findViewById<RecyclerView>(R.id.fragment_chatroomView)
                        .smoothScrollToPosition(msgAdapter.itemCount - 1)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View
        if (inflater != null) {
            rootView = inflater.inflate(R.layout.fragment_chatroom1, container, false)

            val btnSend = rootView.findViewById<ImageButton>(R.id.fragment_btnSend_chatroom)
            val editText = rootView.findViewById<EditText>(R.id.fragment_et_chatroom)
            val chatroomView = rootView.findViewById<RecyclerView>(R.id.fragment_chatroomView)

            btnSend.isEnabled = false
            btnSend.setBackgroundResource(R.drawable.send_icon_disabled)

            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    btnSend.isEnabled = !editText.text.isEmpty()
                    if (btnSend.isEnabled) {
                        btnSend.setBackgroundResource(R.drawable.send_icon)
                    } else {
                        btnSend.setBackgroundResource(R.drawable.send_icon_disabled)
                    }

                }

                override fun afterTextChanged(p0: Editable?) {}
            })

            val mLayoutManager = LinearLayoutManager(context as AppCompatActivity)
            msgAdapter = MsgAdapter(context as AppCompatActivity)
            chatroomView.layoutManager = mLayoutManager
            chatroomView.adapter = msgAdapter

            Connection.registerObserver(this)

            btnSend.setOnClickListener {
                if (!editText.text.isEmpty()) {

                    val msg = editText.text.toString()
                    resetInput(editText)
                    Connection.writeToServer(ChatMessage(msg, LocalDateTime.now().toString(), CurrentUser.user))
                }
            }

            return rootView

        } else {
            return null
        }
    }

    private fun resetInput(editText: EditText) {
        editText.text.clear()
    }
}
