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

    private val newITEMS: MutableList<Licznik> = ArrayList()
    private val newSUBITEMS: MutableList<Oceny> = ArrayList()


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
        val temporary = newITEMS.sortedWith(compareBy { it })
        newITEMS.clear()
        newITEMS.addAll(temporary)

        ITEMS.clear()
        ITEMS.addAll(newITEMS)
        newITEMS.clear()

        SUB_ITEMS.clear()
        SUB_ITEMS.addAll(newSUBITEMS)
        newSUBITEMS.clear()
    }


    private fun addItem(item: Licznik) {
        newITEMS.add(item)
    }

    private fun addSubItem(item: Oceny) {
        newSUBITEMS.add(item)
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