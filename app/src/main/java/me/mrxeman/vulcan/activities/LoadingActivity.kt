package me.mrxeman.vulcan.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.utils.Global.*
import java.lang.NullPointerException
import kotlin.concurrent.thread

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)

        thread {
            mainRequest()
        }
    }

    private fun mainRequest() = thread(isDaemon = true) {
        try {
            user.mainRequest()
        } catch (_: NullPointerException) {
            startActivity(Intent(this, MainActivity::class.java))
        }

        val intent = Intent(this, VulcanActivity::class.java)
        startActivity(intent)
    }
}