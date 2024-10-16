package me.mrxeman.vulcan.activities.ui.sprawdziany

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.sprawdziany.utils.Sprawdziany
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.ChronoField
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class SprawdzianyFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sprzawdziany_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MySprawdzianyRecyclerViewAdapter(Sprawdziany.ITEMS_LIST)
                val optionalDate = Sprawdziany.ITEMS_MAP.keys.stream().filter {
                    it.isBefore(selectedDate)
                }.min(Comparator.comparingLong {
                    it.until(selectedDate, ChronoUnit.DAYS)
                })
                if (optionalDate.isPresent) {
                    Sprawdziany.ITEMS_LIST.forEachIndexed { index, item->
                        if (item.date) {
                            val date: LocalDate = item.extra as LocalDate
                            if (optionalDate.get().isEqual(date)) {
                                view.scrollToPosition(index)
                                return@forEachIndexed
                            }
                        }
                    }
                }
            }
        }
        return view
    }

    companion object {
        var selectedDate: LocalDate = LocalDate.now()
    }

}