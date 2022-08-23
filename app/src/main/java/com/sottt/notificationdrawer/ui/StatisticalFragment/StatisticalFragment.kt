package com.sottt.notificationdrawer.ui.StatisticalFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.databinding.FragmentStatisticalBinding


class StatisticalFragment : Fragment() {

    private var _viewBinding: FragmentStatisticalBinding? = null

    private val viewBinding get() = _viewBinding!!

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


}