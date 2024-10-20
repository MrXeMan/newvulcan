package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.sredniap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.mrxeman.vulcan.R

class StatFrekwencjaSredniaPrzedmiotowaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_stat_frekwencja_srednia_przedmiotowa,
            container,
            false
        )
    }
}