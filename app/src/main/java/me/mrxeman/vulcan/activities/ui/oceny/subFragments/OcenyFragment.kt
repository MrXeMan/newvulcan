package me.mrxeman.vulcan.activities.ui.oceny.subFragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.oceny.utils.Oceny

class OcenyFragment : Fragment() {

    private var columnCount = 1
    private lateinit var adapter1: MyOcenyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ocena_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                this.adapter = MyOcenyRecyclerViewAdapter(Oceny.selectedPrzedmiot!!.oceny)
                adapter1 = this.adapter as MyOcenyRecyclerViewAdapter
            }
        }
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        adapter1.notifyDataSetChanged()
    }
}