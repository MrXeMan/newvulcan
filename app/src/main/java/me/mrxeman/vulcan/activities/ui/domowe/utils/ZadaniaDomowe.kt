package me.mrxeman.vulcan.activities.ui.domowe.utils

import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Extensions.asLocalDate
import java.time.LocalDate
import java.util.ArrayList
import java.util.HashMap
import java.util.SortedMap

object ZadaniaDomowe {


    val ITEMS: MutableList<ZadanieDomowe> = ArrayList()
    val ITEMS_LIST: MutableList<Item> = ArrayList()
    val ITEMS_MAP: SortedMap<LocalDate, ArrayList<ZadanieDomowe>> = sortedMapOf()

    fun load(temporary: String, temporary2: String) {
        val topLevel = JsonParser.parseString(temporary).asJsonArray
        topLevel.forEach {
            val zd = it.asJsonObject
            if (zd.get("typ").asInt != 4) {
                return@forEach
            }
            val id: Int = zd.get("id").asInt
            val dod = JsonParser.parseString(temporary2).asJsonObject
            var desc = ""
            if (dod.get("id").asInt == id) {
                desc = dod.get("opis").asString
            }
            addItem(ZadanieDomowe(
                id,
                zd.get("przedmiotNazwa").asString,
                zd.get("data").asLocalDate,
                zd.get("hasAttachment").asBoolean,
                desc
            ))
        }
        ITEMS_MAP.forEach { (date, list) ->
            ITEMS_LIST.add(Item(true, date))
            list.forEach {
                ITEMS_LIST.add(Item(false, it))
            }
        }
    }

    private fun addItem(item: ZadanieDomowe) {
        ITEMS.add(item)
        if (ITEMS_MAP.containsKey(item.date)) {
            ITEMS_MAP[item.date]!!.add(item)
        } else {
            ITEMS_MAP[item.date] = arrayListOf(item)
        }
    }

    fun getType(type: Int): String? {
        return when (type) {
            1 -> "Sprawdzian"
            2 -> "Kartkowka"
            3 -> "Praca klasowa"
            4 -> "Praca domowa"
            else -> null
        }
    }

    data class ZadanieDomowe(val id: Int, val przedmiot: String, val date: LocalDate, val attach: Boolean, val description: String)

    data class Item(val date: Boolean, val extra: Any)
}