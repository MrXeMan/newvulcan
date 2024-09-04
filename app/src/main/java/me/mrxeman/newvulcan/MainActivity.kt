package me.mrxeman.newvulcan

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import io.ktor.http.Cookie
import me.mrxeman.newvulcan.Extras.Global
import me.mrxeman.newvulcan.Extras.Global.loadCookies
import me.mrxeman.newvulcan.Extras.Global.saveCookies
import me.mrxeman.newvulcan.Extras.MyApplication
import me.mrxeman.newvulcan.eduvulcan.Account
import me.mrxeman.newvulcan.exceptions.LoggedNoFoundUserStringException
import me.mrxeman.newvulcan.exceptions.NoCookiesException
import me.mrxeman.newvulcan.exceptions.VerificationException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val sharedPreferences: SharedPreferences = MyApplication.getContext().getSharedPreferences("APP", Context.MODE_PRIVATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailText: TextView = findViewById(R.id.editTextTextEmailAddress)
        val passText: TextView = findViewById(R.id.editTextTextPassword)
        val logInButton: Button = findViewById(R.id.LogInButton)

        addCookies()
        loadCookies()

        if (sharedPreferences.getBoolean("loggedIn", false)) {
            println("Found logged in boolean!")
            lateinit var user: User
            try {
                user = User.createUserFromString(sharedPreferences.getString("userString", null))
            } catch (e: LoggedNoFoundUserStringException) {
                sharedPreferences.edit {
                    putBoolean("loggedIn", false)
                }
                println(e)
            }
        }
        println("Didn't find found logged in boolean!")

        lateinit var user: User

        logInButton.setOnClickListener {
            println("Logging in...")
            val email: String = emailText.text.toString()
            val password: String = passText.text.toString()
            user = User(email, password)
            thread {
                try {
                    println("Trying to log in...")
                    user.logIn()
                    if (user.loggedIn) {
                        println("Logged in successfully!")
                        val acc = Account(user)
                        acc.getKey()
                    } else {
                        println("Failed to log in... the cause is unknown...")
                    }
                } catch (e: VerificationException) {
                    println(e)
                } catch (e: NoCookiesException) {
                    println(e)
                }
            }
        }

    }

    private fun addCookies() {
        if (sharedPreferences.getBoolean("firstrun", true)) {
            val cookies: ArrayList<Cookie> = ArrayList()
            cookies.add(Cookie("ARRAffinity", "6e2f93db702a4fb8a550d4857b6d8fcc649caa6683563512713f6617de5c2438", domain="eduvulcan.pl", path="/"))
            cookies.add(Cookie("ARR_3S_ARR_EDUVULCAN", "cbf757772d8c7b0c717aec4086e6e59f277c2d9600656cf5f88113b1fae81c27", domain="eduvulcan.pl", path="/"))
            Global.addCookies(cookies)
            sharedPreferences.edit {
                putBoolean("firstrun", false)
            }
            saveCookies()
        }
    }
}