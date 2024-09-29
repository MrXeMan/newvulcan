package me.mrxeman.vulcan.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.utils.Global
import me.mrxeman.vulcan.utils.User
import java.lang.RuntimeException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val emailText = findViewById<EditText>(R.id.EmailAddress)
        val passwdText = findViewById<EditText>(R.id.PasswordText)
        val logInButton = findViewById<Button>(R.id.logInButton)
        var user = try {
            println("Loading user...")
            User.load()
        } catch (e: RuntimeException) {
            println("Failed!")
            println(e)
            null
        }

        if (user != null) {
            logIn(user)
        }

        logInButton.setOnClickListener {
            Global.run {
                val email: String = emailText.text.toString()
                val passwd: String = passwdText.text.toString()
                user = User(email, passwd)
                logIn(user!!, false)
            }
        }

    }

    private fun logIn(user: User, loaded: Boolean = true) {
        try {
            if (!loaded) user.save()
            println("Logged: $loaded")
            user.PageLogIn()
            Global.user = user
            val intent = Intent(this, LoadingActivity::class.java)
            startActivity(intent)
        } catch (e: RuntimeException) {
            println("ERROR: $e")
        }
    }




}