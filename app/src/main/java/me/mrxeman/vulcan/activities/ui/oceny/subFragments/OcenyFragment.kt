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


/**
 * A fragment representing a list of Items.
 */
class OcenyFragment : Fragment() {

    private var columnCount = 1
    private lateinit var adapter1: MyOcenyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
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

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
                OcenyFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}