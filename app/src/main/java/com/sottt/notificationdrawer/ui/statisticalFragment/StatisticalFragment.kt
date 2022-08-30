package com.sottt.notificationdrawer.ui.statisticalFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.Description
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
    }

    fun iniCharts() {
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


}