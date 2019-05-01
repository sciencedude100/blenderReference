package io.ejg.blenderreference

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.MenuItem
import android.support.v4.widget.DrawerLayout
import android.support.design.widget.NavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.webkit.WebView
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*


/*This Presentation is brought to you by the Stan... StackOverflow center for professional development.

 https://youtu.be/KkMDCCdjyW8?t=10
* */

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var displayWebView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        // set default webpage
        displayWebView = findViewById<WebView>(R.id.webview)
        displayWebView!!.settings.javaScriptEnabled = true
        displayWebView!!.loadUrl("https://docs.blender.org/manual/en/latest/")
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // This is probably my favoite thing about Kotlin: formatting of "when"/switch-case statements
            R.id.action_settings -> {
                val view = layoutInflater.inflate(R.layout.popup, null)
                val popup : PopupWindow = PopupWindow(
                    view,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) popup.elevation = 10.0F
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val slideIn = Slide()
                    slideIn.slideEdge = Gravity.TOP
                    popup.enterTransition = slideIn
                    val slideOut = Slide()
                    slideOut.slideEdge = Gravity.RIGHT
                    popup.exitTransition = slideOut
                }
                val close = view.findViewById<Button>(R.id.button_popup)
                close.setOnClickListener{
                    popup.dismiss()
                }
                popup.setOnDismissListener {
                    Toast.makeText(applicationContext, "Popup Dead", Toast.LENGTH_SHORT).show()
                }
                TransitionManager.beginDelayedTransition(drawer_layout)
                popup.showAtLocation(
                    drawer_layout,
                    Gravity.CENTER,
                    0, 0
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // manage the hamburger stack here
        when (item.itemId) {
            R.id.nav_home -> {
                // Main Docs
                displayWebView!!.loadUrl("https://docs.blender.org/manual/en/latest/")
            }
            R.id.nav_gallery -> {
                // Python API
                displayWebView!!.loadUrl("https://docs.blender.org/api/current/")
            }
            R.id.nav_slideshow -> {
                // Dev Docs
                displayWebView!!.loadUrl("https://wiki.blender.org/wiki/Main_Page")
            }
            R.id.nav_tools -> {
                // Ask for help
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://blender.stackexchange.com/"))
                startActivity(browserIntent)
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
