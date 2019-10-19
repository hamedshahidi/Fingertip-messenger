package HamedShahidi.fingertipmessenger

import android.app.Application
import android.content.res.Resources
import android.util.Log

class App : Application() {

    companion object {
        var instance: App? = null
            private set
        var res: Resources? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("App", "App/onCreat: App class created")
        instance = this
        res = resources
    }
}