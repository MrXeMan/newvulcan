package me.mrxeman.vulcan.activities.ui.statystyki.oceny.licznik.licznik

import java.time.Instant
import java.util.Date
import java.util.HashMap
import kotlin.collections.ArrayList

object Licznik {

    val ITEMS: MutableList<Licznik> = ArrayList()
    val SUB_ITEMS: MutableList<Oceny> = ArrayList()

    val ITEM_MAP: MutableMap<String, Licznik> = HashMap()
    val SUB_ITEM_MAP: MutableMap<String, Oceny> = HashMap()


    fun load() {
        val map: MutableMap<String, Int> = mutableMapOf()
        me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny.przedmioty.forEach {
            it.oceny.forEach { ocena ->
                map.putIfAbsent(ocena.grade, 0)
                map[ocena.grade] = map[ocena.grade]!! + 1
                addSubItem(
                    Oceny(
                        it.name,
                        Date.from(ocena.data?.let { it1 -> Instant.ofEpochMilli(it1) }) ?: null,
                        ocena.grade
                    )
                )
            }
        }
        map.forEach { (t, u) ->
            addItem(
                Licznik(
                    t, u
                )
            )
        }
        val temporary = ITEMS.sortedWith(compareBy { it })
        ITEMS.clear()
        ITEMS.addAll(temporary)
    }


    private fun addItem(item: Licznik) {
        ITEMS.add(item)
        ITEM_MAP[item.grade] = item
    }

    private fun addSubItem(item: Oceny) {
        SUB_ITEMS.add(item)
        SUB_ITEM_MAP[item.name] = item
    }


    data class Oceny(val name: String, val date: Date?, val grade: String)

    data class Licznik(val grade: String, val number: Int) : Comparable<Licznik> {

        override fun compareTo(other: Licznik): Int {
            return when {
                other.grade.first().isDigit() && this.grade.first().isDigit() -> {
                    if (other.grade.first().digitToInt() > this.grade.first().digitToInt()) {
                        1
                    } else if (other.grade.first().digitToInt() < this.grade.first().digitToInt()) {
                        -1
                    } else {
                        0
                    }
                }
                other.grade.first().isDigit() && !this.grade.first().isDigit() -> 1
                !other.grade.first().isDigit() && this.grade.first().isDigit() -> -1
                !other.grade.first().isDigit() && !this.grade.first().isDigit() -> 0
                else -> 0
            }
        }


    }

}