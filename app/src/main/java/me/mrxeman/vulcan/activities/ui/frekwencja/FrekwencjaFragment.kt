package me.mrxeman.vulcan.activities.ui.frekwencja

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.frekwencja.utils.Obecnosc
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * A fragment representing a list of Items.
 */
class FrekwencjaFragment : Fragment() {

    private var columnCount = 1
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: MyFrekwencjaRecyclerViewAdapter
    var datePicker: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_frekwencja_list, container, false)

        recyclerView = view.findViewById(R.id.list)

        // Set the adapter
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            setAdapter()
        }

        datePicker = view.findViewById(R.id.date_picker)
        datePicker!!.setOnClickListener {
            val newFragment = DatePickerFragment()
            newFragment.show(childFragmentManager, "datePicker")
        }
        datePicker!!.text = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))

        val nextButton: ImageButton = view.findViewById(R.id.button_next)
        val prevButton: ImageButton = view.findViewById(R.id.button_prev)

        nextButton.setOnClickListener {
            selectedDate = selectedDate.plusDays(1)
            datePicker!!.text = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            setAdapter()
        }
        prevButton.setOnClickListener {
            selectedDate = selectedDate.minusDays(1)
            datePicker!!.text = selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            setAdapter()
        }

        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        adapter.notifyDataSetChanged()
        super.onResume()
    }

    fun setAdapter() {
        this@FrekwencjaFragment.adapter = MyFrekwencjaRecyclerViewAdapter(Obecnosc.ITEMS_MAP[selectedDate]?.frekwencja ?: mutableListOf())
        recyclerView.adapter = adapter
    }

    companion object {
        var selectedDate: LocalDate = LocalDate.now()
    }

}