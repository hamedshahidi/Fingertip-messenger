package HamedShahidi.fingertipmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.WindowManager
import android.widget.*
import kotlinx.android.synthetic.main.activity_login.*
import java.time.LocalTime
import kotlin.concurrent.thread

class LoginAct : AppCompatActivity(), ChatObserver {

    var username = ""
    var userIsSet = false
    var resume = false
    var attempts = 0

    override fun updateMessage(msg: ChatMessage) {

        when (msg.message) {

            ">> Username is set to \"$username\"" -> {
                userIsSet = true
                val intentChatroom = Intent(this, TabbedAct::class.java)
                startActivity(intentChatroom)
                finish()
            }

            ">> $username already exists." -> {
                runOnUiThread { Toast.makeText(this, "This username is taken!", Toast.LENGTH_SHORT).show() }
            }
            null -> {
                Log.d("LoginAct", "Login/updateMessage/ NOTok(null): connected= ${Connection.connected} userIsSet=$userIsSet")
            }
            "" -> {
                Log.d("LoginAct", "Login/updateMessage/ NOTok(\"\"): connected= ${Connection.connected} userIsSet=$userIsSet")
            }
            else -> {
                Log.d("LoginAct", "Login/updateMessage/ NOTok(else): connected= ${Connection.connected} userIsSet=$userIsSet")            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref.edit()
        Connection.addPref(sharedPref)

        Connection.registerObserver(this)

        btnLogin.setOnClickListener {
            Log.d("**", "Login/ClickListener(start): userIsSet=$userIsSet username=$username")

            if (!et_login.text.isEmpty()) {
                username = et_login.text.toString()
                editor.putString(getString(R.string.fingertip_username), username)
                editor.apply()
                Connection.addPref(sharedPref)

                CurrentUser.user = username

                if (!Connection.connected) {
                    thread {
                        Connection.connect()
                    }
                }

                do {
                    if (Connection.connected) resume = true
                    attempts++
                } while (!resume && attempts < 500)

                if (resume && !userIsSet) {
                    Connection.writeToServer(
                            ChatMessage(
                                    ":user $username",
                                    LocalTime.now().toString(),
                                    username))

                } else {
                    runOnUiThread { Toast.makeText(this, "Unable to connect...", Toast.LENGTH_SHORT).show() }
                    attempts = 0
                }

            } else {
                Toast.makeText(applicationContext, "Please provide a username", Toast.LENGTH_SHORT).show()
            }
        }

        btnMenuLogin.setOnClickListener { menuItem ->
            val popup = PopupMenu(this, menuItem)
            val inflater = popup.menuInflater as MenuInflater
            inflater.inflate(R.menu.login_menu, popup.menu)
            popup.show()

            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.ipAddress -> {
                        val intentIP = Intent(this, IpAct::class.java)
                        startActivity(intentIP)
                        true
                    }
                    R.id.about -> TODO() //about text
                    else -> super.onOptionsItemSelected(it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)
        return true
    }
}
