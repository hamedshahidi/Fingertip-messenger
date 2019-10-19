package HamedShahidi.fingertipmessenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.NumberPicker
import kotlinx.android.synthetic.main.activity_ip.*

class IpAct(var costumeIP: String = "...") : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ip)

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(this)
        val editor = sharedPref.edit()

        val np1 = findViewById<NumberPicker>(R.id.ip1)
        val np2 = findViewById<NumberPicker>(R.id.ip2)
        val np3 = findViewById<NumberPicker>(R.id.ip3)
        val np4 = findViewById<NumberPicker>(R.id.ip4)

        np1.minValue = 0
        np1.maxValue = 255
        np1.wrapSelectorWheel = true
        np1.value = sharedPref.getInt("ip1", 78)

        np2.minValue = 0
        np2.maxValue = 255
        np2.wrapSelectorWheel = true
        np2.value = sharedPref.getInt("ip2", 0)


        np3.minValue = 0
        np3.maxValue = 255
        np3.wrapSelectorWheel = true
        np3.value = sharedPref.getInt("ip3", 0)

        np4.minValue = 0
        np4.maxValue = 255
        np4.wrapSelectorWheel = true
        np4.value = sharedPref.getInt("ip4", 2)

        btnIP.setOnClickListener {
            val ip1 = np1.value
            val ip2 = np2.value
            val ip3 = np3.value
            val ip4 = np4.value

            costumeIP = "$ip1.$ip2.$ip3.$ip4"

            editor.putInt("ip1", ip1)
            editor.putInt("ip2", ip2)
            editor.putInt("ip3", ip3)
            editor.putInt("ip4", ip4)
            editor.putString(getString(R.string.fingertip_ip), costumeIP)
            editor.apply()
            finish()
        }
    }
}
