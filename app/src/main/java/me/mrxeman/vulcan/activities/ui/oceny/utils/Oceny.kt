package me.mrxeman.vulcan.activities.ui.oceny.utils

import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Global
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.HashMap

object Oceny {

    var przedmioty: MutableList<Przedmiot> = mutableListOf()

    var selectedPrzedmiot: Przedmiot? = null

    val przedmiotyMap: MutableMap<String, Przedmiot> = mutableMapOf()

    fun load(temporary: String) {
        val topLevel = JsonParser.parseString(temporary).asJsonObject.get("ocenyPrzedmioty").asJsonArray
        topLevel.forEach {
            val przed = it.asJsonObject
            val oceny: MutableList<Ocena> = mutableListOf()
            przed.get("ocenyCzastkowe").asJsonArray.forEach {itt ->
                val oc = itt.asJsonObject
                println("Data: ${oc.get("dataOceny").asString} - ${SimpleDateFormat("dd.MM.yyyy").parse(oc.get("dataOceny").asString)}")
                val o = Ocena(oceny.size,
                    oc.get("wpis").asString,
                    SimpleDateFormat("dd.MM.yyyy").parse(oc.get("dataOceny").asString)!!.time,
                    oc.get("waga").asFloat.toInt(),
                    oc.get("nazwaKolumny").asString,
                    let {
                        if (oc.get("kategoriaKolumny").isJsonNull) {
                            return@let null
                        } else {
                            return@let oc.get("kategoriaKolumny").asString
                        }
                    })
                oceny.add(o)
            }
            val p = Przedmiot(przed.get("przedmiotNazwa").asString,
                let {
                    if (przed.get("nauczyciele").asJsonArray.size() > 0) {
                        return@let przed.get("nauczyciele").asJsonArray.get(0).asString
                    } else {
                        return@let null
                    }
                },
                oceny)
            addPrzedmiot(p)
        }
    }

    private fun addPrzedmiot(przedmiot: Przedmiot) {
        przedmioty.add(przedmiot)
        przedmiotyMap[przedmiot.name] = przedmiot
    }

    data class Przedmiot(val name: String, val teacher: String?, val oceny: MutableList<Ocena>) {
        override fun toString(): String = name

        fun getNewestGrade(): Ocena? {
            val map: MutableMap<Long, Ocena> = mutableMapOf()
            val list: MutableList<Long> = mutableListOf()
            oceny.forEach {
                it.data?.let { it1 -> map[it1] = it }
                it.data?.let { it1 -> list.add(it1) }
            }
            val date = Collections.max(list)
            return map[date]
        }
    }

    data class Ocena(val id: Int, val grade: String, val data: Long?, val importance: Int, val columnName: String, val categoryName: String?)

}