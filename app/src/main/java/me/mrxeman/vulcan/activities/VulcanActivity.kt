package me.mrxeman.vulcan.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.databinding.ActivityVulcanBinding

class VulcanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVulcanBinding
    private val fragments = HashMap<Int, Pair<Int?, Int?>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        load()

        binding = ActivityVulcanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_vulcan)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)

        navView.menu.clear()
        fragments.forEach {
            val item = navView.menu.add(it.key)
            if (it.value.first != null) item.setIcon(it.value.first!!)
            if (it.value.second != null) {
                item.setOnMenuItemClickListener {_ ->
                    navController.navigate(it.value.second!!)
                    return@setOnMenuItemClickListener true
                }
            }
        }

        navView.setupWithNavController(navController)
    }

    private fun load() {
        fragments[R.string.title_dashboard] = R.drawable.ic_dashboard_black_24dp to R.id.navigation_dashboard
        fragments[R.string.title_oceny] = R.drawable.baseline_looks_6_24 to R.id.navigation_oceny
        fragments[R.string.title_lekcje] = R.drawable.ic_home_black_24dp to R.id.navigation_lekcje
        fragments[R.string.title_frekwencja] = R.drawable.ic_home_black_24dp to R.id.navigation_frekwencja
    }
}