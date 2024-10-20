package me.mrxeman.vulcan.activities.ui.statystyki

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.mrxeman.vulcan.R
import me.mrxeman.vulcan.activities.ui.statystyki.frekwencja.StatFrekwencjaFragment
import me.mrxeman.vulcan.activities.ui.statystyki.oceny.StatOcenyFragment

class StatystykiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_statystyki, container, false)

        val tabLayout: TabLayout = view.findViewById(R.id.tabs)
        val pager: ViewPager2 = view.findViewById(R.id.pager)
        pager.adapter = ViewPagerAdapter(childFragmentManager, lifecycle, array.size)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = array[position]
        }.attach()

        return view
    }

    companion object {
        val array = arrayOf(
            "Oceny",
            "Frekwencja"
        )
        val map: Map<String, Fragment> = mapOf(
            "Oceny" to StatOcenyFragment(),
            "Frekwencja" to StatFrekwencjaFragment()
        )
    }

}