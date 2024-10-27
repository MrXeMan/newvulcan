package me.mrxeman.vulcan.activities.ui.sprawdziany.utils

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import me.mrxeman.vulcan.utils.Extensions.asLocalDate
import me.mrxeman.vulcan.utils.Global
import java.time.LocalDate
import java.util.HashMap
import java.util.SortedMap
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

object Sprawdziany {

    val ITEMS: MutableList<Sprawdzian> = ArrayList()
    val ITEMS_LIST: MutableList<Item> = ArrayList()
    val ITEMS_MAP: SortedMap<LocalDate, ArrayList<Sprawdzian>> = sortedMapOf()

    fun load(testsJSON: JsonElement) = thread {
        val topLevel = testsJSON.asJsonArray
        topLevel.forEach {
            val spr = it.asJsonObject
            if (spr.get("typ").asInt == 4) {
                return@forEach
            }
            val id: Int = spr.get("id").asInt
            val dod = Global.user.api.SprawdzianRequest(id).asJsonObject
            var desc: String = ""
            if (dod.get("id").asInt == id) {
                desc = dod.get("opis").asString
            }
            addItem(Sprawdzian(
                id,
                spr.get("typ").asInt,
                spr.get("przedmiotNazwa").asString,
                spr.get("data").asLocalDate,
                spr.get("hasAttachment").asBoolean,
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

    private fun addItem(item: Sprawdzian) {
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
            else -> null
        }
    }

    data class Sprawdzian(val id: Int, val category: Int, val przedmiot: String, val date: LocalDate, val attach: Boolean, val description: String)

    data class Item(val date: Boolean, val extra: Any)
}