package me.mrxeman.newvulcan.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import me.mrxeman.newvulcan.Extras.Global.getApi
import me.mrxeman.newvulcan.R

class SubActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val mainAPI = findViewById<Button>(R.id.mainAPI)
        val markAPI = findViewById<Button>(R.id.ocenyAPI)
        val lessonsAPI = findViewById<Button>(R.id.lessonsAPI)


        mainAPI.setOnClickListener {
            getApi().mainRequest()
        }

        markAPI.setOnClickListener {
            getApi().todayMarksRequest()
        }


    }

}