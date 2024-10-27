package me.mrxeman.vulcan.activities.ui.frekwencja.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object Obecnosc {

    val ITEMS: MutableList<Frekwencje> = mutableListOf()
    val ITEMS_MAP: MutableMap<LocalDate, Frekwencje> = mutableMapOf()

    private val newITEMS: MutableList<Frekwencje> = mutableListOf()
    private val newITEMS_MAP: MutableMap<LocalDate, Frekwencje> = mutableMapOf()

    fun load(frekwencjaJSON: JsonElement) {
        val json = frekwencjaJSON.asJsonArray
        json.forEach {
            val f = it.asJsonObject
            val day = LocalDate.parse(f.get("godzinaOd").asString.split("T")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val frekwencje: MutableList<Frekwencja> = if (newITEMS_MAP.containsKey(day)) {
                newITEMS_MAP[day]!!.frekwencja
            } else {
                mutableListOf()
            }
            frekwencje.add(
                Frekwencja(
                    f.get("kategoriaFrekwencji").asInt,
                    OffsetDateTime.parse(f.get("godzinaOd").asString),
                    OffsetDateTime.parse(f.get("godzinaDo").asString),
                    f.get("numerLekcji").asInt,
                    f.get("opisZajec").asString
                )
            )
            if (!newITEMS_MAP.containsKey(day)) {
                addItem(Frekwencje(day, frekwencje))
            }
        }
        newITEMS_MAP.keys.forEach {
            val temporary: MutableList<Frekwencja>? = newITEMS_MAP[it]?.frekwencja
            if (temporary != null) {
                newITEMS_MAP[it] = Frekwencje(it,
                    temporary.sortedWith(compareBy(Frekwencja::lessonNumber)).toMutableList()
                )
            }
        }
        ITEMS.clear()
        ITEMS.addAll(newITEMS)
        newITEMS.clear()

        ITEMS_MAP.clear()
        ITEMS_MAP.putAll(newITEMS_MAP)
        newITEMS_MAP.clear()
    }

    private fun addItem(item: Frekwencje) {
        newITEMS.add(item)
        newITEMS_MAP[item.day] = item
    }

    data class Frekwencje(val day: LocalDate, val frekwencja: MutableList<Frekwencja>)


    data class Frekwencja(val frekwencjaCategory: Int, val godzinaOd: OffsetDateTime, val godzinaDo: OffsetDateTime, val lessonNumber: Int, val lesson: String)

}