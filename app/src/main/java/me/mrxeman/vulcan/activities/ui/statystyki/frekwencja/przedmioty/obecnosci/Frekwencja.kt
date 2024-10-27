package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.przedmioty.obecnosci

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Extensions.asLocalDate
import me.mrxeman.vulcan.utils.Extensions.asLocalDateTime
import me.mrxeman.vulcan.utils.Extensions.asZonedDateTime
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.ArrayList
import java.util.HashMap

object Frekwencja {

    val ITEMS: MutableList<Frekwencja> = ArrayList()

    private val newITEMS: MutableList<Frekwencja> = ArrayList()

    fun load(frekwencjaJSON: JsonElement) {
        val topLevel = frekwencjaJSON.asJsonArray
        val freq: MutableMap<Int, MutableList<Lekcja>> = mutableMapOf()
        topLevel.forEach {
            val lekcja = Lekcja(
                it.asJsonObject.get("godzinaOd").asZonedDateTime,
                it.asJsonObject.get("godzinaDo").asZonedDateTime,
                it.asJsonObject.get("opisZajec").asString
            )
            freq.putIfAbsent(
                it.asJsonObject.get("kategoriaFrekwencji").asInt,
                mutableListOf()
                )
            freq[it.asJsonObject.get("kategoriaFrekwencji").asInt]!!.add(lekcja)
        }
        freq.forEach { (t, u) ->
            addItem(Frekwencja(t, u))
        }
        newITEMS.forEach { it ->
            val temp = it.lekcje.sortedWith(compareBy<Lekcja> { it.dateOd }.reversed())
            it.lekcje.clear()
            it.lekcje.addAll(temp)
        }
        ITEMS.clear()
        ITEMS.addAll(newITEMS)
        newITEMS.clear()
    }

    private fun addItem(frekwencja: Frekwencja) {
        newITEMS.add(frekwencja)
    }

    data class Frekwencja(val category: Int, val lekcje: MutableList<Lekcja>)

    data class Lekcja(val dateOd: ZonedDateTime, val dateDo: ZonedDateTime, val name: String)

}