package me.mrxeman.vulcan.activities.ui.frekwencja

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FrekwencjaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is frekwencja Fragment"
    }
    val text: LiveData<String> = _text
}