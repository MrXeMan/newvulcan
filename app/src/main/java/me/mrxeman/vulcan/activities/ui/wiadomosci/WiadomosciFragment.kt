package me.mrxeman.vulcan.activities.ui.wiadomosci

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import me.mrxeman.vulcan.R

class WiadomosciFragment : Fragment() {

    val array = arrayOf(
        "Odebrane",
        "Wyslane"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wiadomosci, container, false)

        val tabLayout: TabLayout = view.findViewById(R.id.tabs)
        val pager: ViewPager2 = view.findViewById(R.id.pager)
        pager.adapter = ViewPagerAdapter(parentFragmentManager, lifecycle)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = array[position]
        }.attach()

        return view
    }
}