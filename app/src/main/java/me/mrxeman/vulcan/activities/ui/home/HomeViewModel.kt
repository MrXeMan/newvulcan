package me.mrxeman.vulcan.activities.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonElement
import me.mrxeman.vulcan.utils.Global
import java.util.ArrayList

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    val gradeJSON: LiveData<ArrayList<JsonElement>> = Global.user.api.Oceny
    val frekwencjaJSON: LiveData<JsonElement> = Global.user.api.frekwencjaJson
    val lessonsJSON: LiveData<JsonElement> = Global.user.api.lekcjeJson
    val testsJSON: LiveData<JsonElement> = Global.user.api.testsJson
    val rMessagesJSON: LiveData<JsonElement> = Global.user.api.RMessagesJson
    val sMessagesJSON: LiveData<JsonElement> = Global.user.api.SMessagesJson

}