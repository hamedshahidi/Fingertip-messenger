package HamedShahidi.fingertipmessenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button


import kotlinx.android.synthetic.main.activity_tabbed.*
import kotlinx.android.synthetic.main.logout_dialog.view.*
import java.time.LocalDateTime

class TabbedAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tabbed)
        setSupportActionBar(toolbar)

        setupViewPager(pager)
        tabs.setupWithViewPager(pager)
        tabs.getTabAt(0)?.setIcon(R.drawable.user_icon_26)
        tabs.getTabAt(1)?.setIcon(R.drawable.chatroom_icon)
        tabs.getTabAt(2)?.setIcon(R.drawable.pm_icon)
        pager.offscreenPageLimit = 3

        tabs.getTabAt(1)?.isSelected
        pager.currentItem = 1
    }

    private fun setupViewPager(pager: ViewPager) {

        val tabAdapter = TabPagerAdapter(supportFragmentManager)
        tabAdapter.addFragment(FragmentOnlineUsers())
        tabAdapter.addFragment(FragmentChatroom())
        tabAdapter.addFragment(FragmentPm())
        pager.adapter = tabAdapter

        pager.addOnPageChangeListener(
                TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(object :
                TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab) {
                pager.currentItem = p0.position
                Log.d("active tab", "tab position = ${p0.position}")
                if (p0.position == 0) {
                    Connection.writeToServer(ChatMessage(":users", "", ""))
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.chatroom_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.logout) {
            val logoutDialog = AlertDialog.Builder(this)
            val dialogView = layoutInflater.inflate(R.layout.logout_dialog, null)
            logoutDialog.setView(dialogView)

            dialogView.btnDialogLogout.setText(R.string.dialog_logout)
            dialogView.btnDialogLogout.findViewById<Button>(R.id.btnDialogLogout)
            dialogView.btnDialogLogout.setOnClickListener {
                Connection.writeToServer(ChatMessage(":logout", LocalDateTime.now().toString(), CurrentUser.user))
                Connection.writeToServer(ChatMessage(":quit", LocalDateTime.now().toString(), CurrentUser.user))
                Connection.deregisterObserver(FragmentChatroom())
                Connection.deregisterObserver(FragmentOnlineUsers())
                val intentLogin = Intent(this, LoginAct::class.java)
                this.startActivity(intentLogin)
                this.finish() as TabbedAct
            }
            logoutDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }
}