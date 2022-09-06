package com.sottt.notificationdrawer.ui.statisticalFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
import com.google.android.material.tabs.TabLayoutMediator
import com.sottt.notificationdrawer.NotificationDrawerApplication
import com.sottt.notificationdrawer.databinding.FragmentStatisticalBinding


class StatisticalFragment : Fragment() {

    private var _viewBinding: FragmentStatisticalBinding? = null

    private val viewBinding get() = _viewBinding!!

    private val viewModel by lazy {
        ViewModelProvider(this).get(StatisticalFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _viewBinding = FragmentStatisticalBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniCharts()
        viewModel.loadPieChartsData()
        iniViewPager(this)
    }

    private fun iniCharts() {
        val pieChart = viewBinding.charts
        viewModel.pieChartsDataSet.observe(viewLifecycleOwner) {
            pieChart.data = it
            pieChart.description = Description().apply {
                text = ""
            }
            pieChart.animateXY(1000, 1000)
            pieChart.invalidate()
        }
    }

    private fun iniViewPager(fragment: Fragment) {
        val map = viewModel.createClassificationNotification()
        val packageNames = map.keys.toList()
        val appNames = packageNames.map {
            NotificationDrawerApplication.getAppName(it)
        }
        val viewPagerAdapter = ViewPagerAdapter(fragment, map)
        val viewPager = viewBinding.pager
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(viewBinding.tabLayout, viewPager) { tab, position ->
            tab.text = appNames[position]
        }.attach()
    }

    private fun iniCardList() {
        viewModel.classificationNotification.observe(this) {

        }
    }

}