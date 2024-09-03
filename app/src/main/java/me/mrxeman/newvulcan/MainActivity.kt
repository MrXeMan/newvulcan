package me.mrxeman.newvulcan

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.gson.Gson
import kotlin.concurrent.thread
import kotlin.reflect.KType
import kotlin.reflect.javaType
import kotlin.reflect.typeOf

class MainActivity : AppCompatActivity() {

    val sharedPreferences: SharedPreferences = MyApplication.getContext().getSharedPreferences("APP", Context.MODE_PRIVATE)

    var cookies: MutableMap<String, String> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addCookies()
        loadCookies()

        lateinit var user: User

        val emailText: TextView = findViewById(R.id.editTextTextEmailAddress)
        val passText: TextView = findViewById(R.id.editTextTextPassword)
        val logInButton: Button = findViewById(R.id.LogInButton)

        logInButton.setOnClickListener {
            val Email: String = emailText.text.toString()
            val Password: String = passText.text.toString()
            user = User(Email, Password)
            thread {
                try {
                    if (user.logIn(cookies)) {
                        println("Logged in successfully!")
                    }
                } catch (e: Exception) {
                    println(e)
                }
            }
        }

    }

    private fun addCookies() {
        if (sharedPreferences.getBoolean("firstrun", true)) {
            cookies["ARRAffinity"] = "6e2f93db702a4fb8a550d4857b6d8fcc649caa6683563512713f6617de5c2438"
            cookies["ARR_3S_ARR_EDUVULCAN"] = "cbf757772d8c7b0c717aec4086e6e59f277c2d9600656cf5f88113b1fae81c27"
            cookies["_ga"] = "GA1.1.859489495.1725045331"
            cookies["_ga_D1SWPMTKK6"] = "GS1.1.1725045331.1.0.1725045362.0.0.0"
            sharedPreferences.edit {
                putBoolean("firstrun", false)
            }
            saveCookies()
        }
    }

    fun saveCookies() {

        val gson = Gson()
        val cookiesString = gson.toJson(cookies)

        sharedPreferences.edit {
            putString("cookies", cookiesString)
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun loadCookies() {

        val cookiesString: String = sharedPreferences.getString("cookies", "")!!
        if (cookiesString.isEmpty()) return
        val gson = Gson()
        val type: KType = typeOf<MutableMap<String, String>>()
        cookies = gson.fromJson(cookiesString, type.javaType)
    }
}