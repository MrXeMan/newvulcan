package me.mrxeman.vulcan.activities.ui.wiadomosci.odebrane

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.wiadomosci.utils.MyWiadomoscRecyclerViewAdapter
import me.mrxeman.vulcan.activities.ui.wiadomosci.utils.Messages

class WiadOdeFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_w_odebrane_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyWiadomoscRecyclerViewAdapter(Messages.R_MESSAGES)
            }
        }
        return view
    }
}