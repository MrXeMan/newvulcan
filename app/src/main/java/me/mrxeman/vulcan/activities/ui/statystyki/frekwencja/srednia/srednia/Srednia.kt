package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.srednia.srednia

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.util.ArrayList
import java.util.HashMap

object Srednia {

    val ITEMS: MutableList<Przedmiot> = ArrayList()

    private val newITEMS: MutableList<Przedmiot> = ArrayList()

    fun load(frekwencjaJSON: JsonElement) {
        val topLevel = frekwencjaJSON.asJsonArray
        val freq: MutableMap<String, Pair<Int, Int>> = mutableMapOf()
        topLevel.forEach {
            val przed = it.asJsonObject.get("opisZajec").asString
            if (!freq.containsKey(przed)) freq[przed] = 0 to 0
            freq[przed] = freq[przed]!!.first.inc() to freq[przed]!!.second
            if (it.asJsonObject.get("kategoriaFrekwencji").asInt == 1) freq[przed] = freq[przed]!!.first to freq[przed]!!.second.inc()
        }
        var obec = 0
        var all = 0
        freq.forEach {
            addItem(it.key, it.value.first, it.value.second)
            obec += it.value.second
            all += it.value.first
        }
        newITEMS.add(0, Przedmiot("Calosc", obec.toFloat().div(all)))
        ITEMS.clear()
        ITEMS.addAll(newITEMS)
        newITEMS.clear()
    }

    private fun addItem(przed: String, num1: Int, num2: Int) {
        newITEMS.add(Przedmiot(przed, num2.toFloat().div(num1)))
    }

    data class Przedmiot(val name: String, val proc: Float)

}