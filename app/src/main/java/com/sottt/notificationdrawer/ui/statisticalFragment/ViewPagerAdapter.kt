package com.sottt.notificationdrawer.ui.statisticalFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sottt.notificationdrawer.MainActivity
import com.sottt.notificationdrawer.data.defined.NotificationInfo

class ViewPagerAdapter(
    private val fragment: Fragment,
    no: HashMap<String, ArrayList<NotificationInfo>>,
    private val viewModel: StatisticalFragmentViewModel
) :
    FragmentStateAdapter(fragment) {

    private var map = no

    private val index get() = map.keys.toList()

    override fun createFragment(position: Int): Fragment {
        val fragment =
            ViewPageFragment(viewModel = viewModel, position, fragment.activity as MainActivity)
        fragment.arguments = Bundle().apply {
            val size = map[index[position]]?.size ?: 0
            putInt("SIZE", size)
            for (index_ in 0 until size) {
                putSerializable("ARG${index_}", map[index[position]]?.get(index_))
            }
        }
        return fragment
    }

    override fun getItemCount(): Int {
        return map.size
    }
}