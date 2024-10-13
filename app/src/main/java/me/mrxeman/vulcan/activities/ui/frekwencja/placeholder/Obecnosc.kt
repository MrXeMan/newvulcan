package me.mrxeman.vulcan.activities.ui.frekwencja.placeholder

import com.google.gson.JsonParser
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

object Obecnosc {

    val ITEMS: MutableList<Frekwencje> = mutableListOf()

    val ITEMS_MAP: MutableMap<LocalDate, Frekwencje> = mutableMapOf()

    fun load(jsonString: String) {
        val json = JsonParser.parseString(jsonString).asJsonArray
        json.forEach {
            val f = it.asJsonObject
            val day = LocalDate.parse(f.get("godzinaOd").asString.split("T")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val frekwencje: MutableList<Frekwencja> = if (ITEMS_MAP.containsKey(day)) {
                ITEMS_MAP[day]!!.frekwencja
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
            if (!ITEMS_MAP.containsKey(day)) {
                addItem(Frekwencje(day, frekwencje))
            }
        }
        ITEMS_MAP.keys.forEach {
            val temporary: MutableList<Frekwencja>? = ITEMS_MAP[it]?.frekwencja
            if (temporary != null) {
                ITEMS_MAP[it] = Frekwencje(it,
                    temporary.sortedWith(compareBy(Frekwencja::lessonNumber)).toMutableList()
                )
            }
        }
    }

    private fun addItem(item: Frekwencje) {
        ITEMS.add(item)
        ITEMS_MAP[item.day] = item
    }

    data class Frekwencje(val day: LocalDate, val frekwencja: MutableList<Frekwencja>)


    data class Frekwencja(val frekwencjaCategory: Int, val godzinaOd: OffsetDateTime, val godzinaDo: OffsetDateTime, val lessonNumber: Int, val lesson: String)

}