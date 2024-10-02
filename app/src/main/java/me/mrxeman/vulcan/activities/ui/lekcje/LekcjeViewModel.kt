package me.mrxeman.vulcan.activities.ui.lekcje

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LekcjeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is lekcje Fragment"
    }
    val text: LiveData<String> = _text
}