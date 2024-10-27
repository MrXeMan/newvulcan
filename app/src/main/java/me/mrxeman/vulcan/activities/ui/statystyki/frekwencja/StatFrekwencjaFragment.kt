package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.przedmioty.StatFrekwencjaPrzedmiotFragment
import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.srednia.StatFrekwencjaSredniaFragment

class StatFrekwencjaFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_stat_frekwencja, container, false)

        val tabLayout: TabLayout = view.findViewById(R.id.tabs3)
        val pager: ViewPager2 = view.findViewById(R.id.pager3)
        pager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle, array.size)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = array[position]
        }.attach()

        return view
    }

    companion object {
        val array: Array<String> = arrayOf(
            "Srednia",
            "Przedmioty"
        )
        val map: Map<String, Fragment> = mapOf(
            "Srednia" to StatFrekwencjaSredniaFragment(),
            "Przedmioty" to StatFrekwencjaPrzedmiotFragment()
        )
    }
}