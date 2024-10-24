package me.mrxeman.vulcan.activities.ui.statystyki.oceny.srednia.srednia

import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import java.util.ArrayList
import java.util.HashMap

object Srednia {

    val ITEMS: MutableList<Przedmiot> = ArrayList()

    val ITEM_MAP: MutableMap<String, Przedmiot> = HashMap()

    fun load() {
        ITEMS.clear()
        ITEM_MAP.clear()
        val temporary: MutableList<Oceny.Ocena> = mutableListOf()
        Oceny.przedmioty.forEach {
            var suma = 0.0F
            var licznik = 0
            it.oceny.forEach { ocena ->
                if (convert(ocena.grade) != null) {
                    suma += convert(ocena.grade)!! * ocena.importance
                    licznik += ocena.importance
                    temporary.add(ocena)
                }
            }
            addItem(Przedmiot(it.name, it.oceny, suma/licznik))
        }
        var srednia = 0.0F
        var licznik = 0
        ITEMS.forEach {
            if (!it.srednia.isNaN()) {
                srednia += it.srednia
                licznik += 1
            }
        }
        ITEMS.add(0, Przedmiot("Wszystko", temporary, srednia/licznik))
    }

    private fun addItem(item: Przedmiot) {
        ITEMS.add(item)
        ITEM_MAP[item.name] = item
    }

    private fun convert(grade: String): Float? {
        val i: Float = when {
            Regex("0").containsMatchIn(grade) -> 0.0F
            Regex("1").containsMatchIn(grade) -> 1.0F
            Regex("2").containsMatchIn(grade) -> 2.0F
            Regex("3").containsMatchIn(grade) -> 3.0F
            Regex("4").containsMatchIn(grade) -> 4.0F
            Regex("5").containsMatchIn(grade) -> 5.0F
            Regex("6").containsMatchIn(grade) -> 6.0F
            else -> return null
        }
        return if (i == 0.0F) {
            i
        } else {
            if (grade.contains("-")) {
                i - 0.25F
            } else if (grade.contains("+")) {
                i + 0.25F
            } else {
                i
            }
        }
    }

    data class Przedmiot(val name: String, val oceny: MutableList<Oceny.Ocena>, val srednia: Float)
}