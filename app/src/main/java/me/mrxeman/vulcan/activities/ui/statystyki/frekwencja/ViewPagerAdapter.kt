package me.mrxeman.vulcan.activities.ui.statystyki.frekwencja

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.mrxeman.vulcan.activities.ui.wiadomosci.odebrane.WiadOdeFragment
import me.mrxeman.vulcan.activities.ui.wiadomosci.wyslane.WiadWysFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle, private val tabs: Int) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return tabs
    }

    override fun createFragment(position: Int): Fragment {
        return StatFrekwencjaFragment.map[StatFrekwencjaFragment.array[position]]!!
    }

}