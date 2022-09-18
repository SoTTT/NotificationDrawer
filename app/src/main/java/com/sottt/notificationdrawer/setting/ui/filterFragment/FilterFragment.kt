package com.sottt.notificationdrawer.setting.ui.filterFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {

    private var _viewBinding: FragmentFilterBinding? = null

    private val viewBinding get() = _viewBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentFilterBinding.inflate(inflater, container, false)
        val linear = viewBinding.linearView
        inflater.inflate(R.layout.filter_info, linear)
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

}