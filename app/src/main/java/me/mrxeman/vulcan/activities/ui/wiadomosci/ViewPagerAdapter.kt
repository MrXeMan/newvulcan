package me.mrxeman.vulcan.activities.ui.wiadomosci

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.mrxeman.vulcan.activities.ui.wiadomosci.odebrane.WiadOdeFragment
import me.mrxeman.vulcan.activities.ui.wiadomosci.wyslane.WiadWysFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            WiadOdeFragment()
        } else {
            WiadWysFragment()
        }
    }

    companion object {
        private const val NUM_TABS = 2
    }

}