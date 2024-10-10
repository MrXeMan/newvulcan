package me.mrxeman.vulcan.activities.ui.oceny.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Global
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

object Oceny {

    val przedmioty: MutableList<Przedmiot> = mutableListOf()

    val przedmiotyMap: MutableMap<String, Przedmiot> = mutableMapOf()

    fun load() {
        if (Global.temporary == null) return
        val topLevel = JsonParser.parseString(Global.temporary).asJsonObject.get("ocenyPrzedmioty").asJsonArray
        topLevel.forEach {
            val przed = it.asJsonObject
            val oceny: MutableList<Ocena> = mutableListOf()
            przed.get("ocenyCzastkowe").asJsonArray.forEach {itt ->
                val oc = itt.asJsonObject
                val o = Ocena(oceny.size,
                    oc.get("wpis").asString,
                    SimpleDateFormat("dd.mm.yyyy").parse(oc.get("dataOceny").asString)!!,
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
    }

    data class Ocena(val id: Int, val grade: String, val data: Date, val importance: Int, val columnName: String, val categoryName: String?) {

    }

}