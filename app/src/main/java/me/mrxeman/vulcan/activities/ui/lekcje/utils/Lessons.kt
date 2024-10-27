package me.mrxeman.vulcan.activities.ui.lekcje.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Extensions.asIntOrNull
import me.mrxeman.vulcan.utils.Extensions.asLocalDate
import me.mrxeman.vulcan.utils.Extensions.asLocalDateOrNull
import me.mrxeman.vulcan.utils.Extensions.asLocalTime
import me.mrxeman.vulcan.utils.Extensions.asLocalTimeOrNull
import me.mrxeman.vulcan.utils.Extensions.asStringOrNull
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Lessons {

    val ITEMS: MutableList<Lekcje> = mutableListOf()
    val ITEMS_MAP: MutableMap<LocalDate, Lekcje> = mutableMapOf()

    private val newITEMS: MutableList<Lekcje> = mutableListOf()
    private val newITEMS_MAP: MutableMap<LocalDate, Lekcje> = mutableMapOf()

    private var failedLekcja: Lekcja? = null

    fun load(lessonsJSON: JsonElement) {
        val topLevel = lessonsJSON.asJsonArray
        topLevel.forEachIndexed { i, it ->
            val godzina = it.asJsonObject
            val day = LocalDate.parse(godzina.get("data").asString.split("T")[0], DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val lekcje: MutableList<Lekcja> = if (newITEMS_MAP.containsKey(day)) {
                newITEMS_MAP[day]!!.lekcje
            } else {
                mutableListOf()
            }
            val zmiany: JsonArray = godzina.get("zmiany").asJsonArray
            val zmiana: Zmiana? = if (zmiany.isEmpty) {
                null
            } else {
                val temp = zmiany.get(0).asJsonObject
                Zmiana(
                    temp.get("zmiana").asInt,
                    temp.get("dzien").asLocalDateOrNull,
                    temp.get("nrLekcji")?.asIntOrNull,
                    temp.get("godzinaOd").asLocalTimeOrNull,
                    temp.get("godzinaDo").asLocalTimeOrNull,
                    temp.get("zajecia")?.asStringOrNull,
                    temp.get("sala")?.asStringOrNull,
                    temp.get("prowadzacy")?.asStringOrNull,
                    temp.get("informacjeNieobecnosc")?.asStringOrNull,
                    null
                )
            }

            if (failedLekcja != null) {
                zmiana?.failedLesson = failedLekcja
                failedLekcja = null
            }

            val lekcja =
                Lekcja(
                    godzina.get("godzinaDo").asLocalDate,
                    godzina.get("godzinaOd").asLocalTime,
                    godzina.get("godzinaDo").asLocalTime,
                    godzina.get("prowadzacy").asString,
                    godzina.get("przedmiot").asString,
                    godzina.get("podzial").asStringOrNull,
                    godzina.get("sala").asString,
                    zmiana
                )

            if (zmiana != null && i + 1 < topLevel.size()) {
                 if (zmiana.kategoria == 1) {
                     if (topLevel.get(i + 1).asJsonObject.get("godzinaOd").asString == godzina.get("godzinaOd").asString) {
                         failedLekcja = lekcja
                         return@forEachIndexed
                     }
                 }
            }

            lekcje.add(lekcja)

            if (!newITEMS_MAP.containsKey(day)) {
                addItem(Lekcje(day, lekcje))
            }
        }
        ITEMS.clear()
        ITEMS.addAll(newITEMS)
        newITEMS.clear()

        ITEMS_MAP.clear()
        ITEMS_MAP.putAll(newITEMS_MAP)
        newITEMS_MAP.clear()
    }



    private fun addItem(item: Lekcje) {
        newITEMS.add(item)
        newITEMS_MAP[item.day] = item
    }


    data class Lekcje(val day: LocalDate, val lekcje: MutableList<Lekcja>)

    data class Lekcja(val date: LocalDate, val godzinaOd: LocalTime, val godzinaDo: LocalTime, val nauczyciel: String,
        val przedmiot: String, val grupa: String?, val sala: String, val zmiany: Zmiana?)

    data class Zmiana(val kategoria: Int, val date: LocalDate?, val lessonNumber: Int?, val godzinaOd: LocalTime?, val godzinaDo: LocalTime?,
                      val przedmiot: String?, val sala: String?, val nauczyciel: String?, val extra: String?, var failedLesson: Lekcja?) {
    }

}