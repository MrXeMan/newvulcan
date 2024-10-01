package me.mrxeman.vulcan.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.utils.Global.*

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)

        user.mainRequest()

        val intent = Intent(this, VulcanActivity::class.java)
        startActivity(intent)
    }
}