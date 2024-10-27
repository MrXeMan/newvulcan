package me.mrxeman.vulcan.activities.ui.oceny

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny
import me.mrxeman.vulcan.utils.MyApplication

class OcenyFragment : Fragment() {

    private var columnCount = 1

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyOcenyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_oceny_list, container, false)

        recyclerView = view.findViewById(R.id.list)

        val searchView: SearchView = view.findViewById(R.id.searchView)
        searchView.clearFocus()

        searchView.setOnQueryTextListener(object : OnQueryTextListener, android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })

        // Set the adapter
        with(recyclerView) {
            layoutManager = when {
                columnCount <= 1 -> LinearLayoutManager(context)
                else -> GridLayoutManager(context, columnCount)
            }
            adapter = MyOcenyRecyclerViewAdapter(Oceny.przedmioty)
            this@OcenyFragment.adapter = adapter as MyOcenyRecyclerViewAdapter
        }
        return view
    }

    private fun filterList(text: String?) {
        val filteredList: MutableList<Oceny.Przedmiot> = mutableListOf()
        Oceny.przedmioty.forEach {
            if (text != null) {
                if (it.name.contains(text,  true)) {
                    filteredList.add(it)
                }
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(MyApplication.getContext(), "No przedmiot found!", Toast.LENGTH_SHORT).show()
            adapter.setFilteredList(null)
        } else {
            adapter.setFilteredList(filteredList)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        adapter.notifyDataSetChanged()
        super.onResume()
    }
}
