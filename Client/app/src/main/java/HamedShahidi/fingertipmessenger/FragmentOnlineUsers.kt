package HamedShahidi.fingertipmessenger

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FragmentOnlineUsers : Fragment(), ChatObserver {

    lateinit var userAdapter: UserAdapter

    override fun updateMessage(msg: ChatMessage) {
        val view = view
        if (view != null && msg.name == "Online users#") {
            activity?.runOnUiThread {
                userAdapter.addUserList(msg)
                val onlineUserView = view.findViewById<RecyclerView>(R.id.fragment_onlineUsersView)
                if (userAdapter.itemCount > 0) {
                    onlineUserView.smoothScrollToPosition(userAdapter.itemCount - 1)
                }
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View
        if (inflater != null) {
            rootView = inflater.inflate(R.layout.fragment_online_users, container, false)

            val onlineUsersView = rootView.findViewById<RecyclerView>(R.id.fragment_onlineUsersView)
            val mLayoutManager = LinearLayoutManager(context as AppCompatActivity)
            userAdapter = UserAdapter(context as AppCompatActivity)

            onlineUsersView.layoutManager = mLayoutManager
            onlineUsersView.adapter = userAdapter

            Connection.registerObserver(this)

            return rootView

        } else {
            return null
        }
    }
}