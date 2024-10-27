package me.mrxeman.vulcan.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.utils.Global
import me.mrxeman.vulcan.utils.Global.*
import java.lang.NullPointerException
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.concurrent.thread

class LoadingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loading)

        mainRequest()
    }

    private fun mainRequest() = thread {
        try {
            user.mainRequest()
        } catch (_: NullPointerException) {
            startActivity(Intent(this, MainActivity::class.java))
        } catch (e: RuntimeException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        }
        val intent = Intent(this, VulcanActivity::class.java)
        startActivity(intent)
        backgroundRequests()
    }

    private fun backgroundRequests() = thread {
        var lastTime: Long = Instant.now().toEpochMilli()
        while (true) {
            for (i in 0..(reloadCooldown/100)) {
                Thread.sleep(100)
                if (reloadRequest) {
                    Log.d("RELOAD", "User just requested a reload!")
                    break
                }
            }
            user.api.loadRequest()
            reloadRequest = false
            Log.d("RELOAD", "Reloading the requests! - last reload was ${Instant.now().toEpochMilli() - lastTime}ms ago...")
            lastTime = Instant.now().toEpochMilli()
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {

    }
}