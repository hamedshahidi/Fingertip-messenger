package HamedShahidi.fingertipmessenger

import android.app.Application

class CurrentUser : Application() {
    companion object {
        lateinit var user: String
    }
}