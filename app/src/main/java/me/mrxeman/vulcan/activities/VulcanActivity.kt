package me.mrxeman.vulcan.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.settings.SettingsActivity
import me.mrxeman.vulcan.databinding.ActivityVulcanBinding
import me.mrxeman.vulcan.utils.Global
import me.mrxeman.vulcan.utils.MyApplication

class VulcanActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVulcanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVulcanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarVulcan.toolbar)

        binding.appBarVulcan.reloadButton.setOnClickListener {
            Global.reloadRequest = true
        }

//        binding.appBarVulcan.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null)
//                .setAnchorView(R.id.fab).show()
//        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_vulcan)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_oceny, R.id.nav_frekwencja, R.id.nav_lekcje, R.id.nav_sprawdziany, R.id.nav_domowe, R.id.nav_wiadomosci, R.id.nav_statystyki
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.isSaveEnabled = true
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.vulcan, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_vulcan)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            startActivity(Intent(MyApplication.getContext(), SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}