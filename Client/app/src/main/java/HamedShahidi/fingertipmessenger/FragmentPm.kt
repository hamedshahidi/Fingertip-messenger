package HamedShahidi.fingertipmessenger

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class FragmentPm : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View
        return if (inflater != null) {
            rootView = inflater.inflate(R.layout.fragment_pm, container, false)
            rootView
        } else {
            null
        }
    }
}