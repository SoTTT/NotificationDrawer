package com.sottt.notificationdrawer.setting.ui.filterFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.databinding.FragmentFilterBinding
import com.sottt.notificationdrawer.filter.AbstractFilter

class FilterFragment : Fragment() {

    private var _viewBinding: FragmentFilterBinding? = null

    private val viewBinding get() = _viewBinding!!

    private val viewModel by lazy {
        ViewModelProvider(this).get(FilterFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentFilterBinding.inflate(inflater, container, false)
        val linear = viewBinding.linearView
        val filters = Repository.getFilters()
        if (filters.isEmpty()) {
            inflater.inflate(R.layout.fragment_filter_blank, viewBinding.linearView, true)
        } else {
            createFilterCards(filters, inflater, viewBinding.linearView)
        }
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    private fun createFilterCards(
        filters: List<AbstractFilter>,
        inflater: LayoutInflater,
        parent: ViewGroup
    ) {
        for (filter in filters) {
            inflater.inflate(R.layout.filter_info, parent).apply {
                findViewById<TextView>(R.id.filter_name).apply {
                    text = filter.name
                }
                findViewById<TextView>(R.id.filter_tag).apply {
                    text = filter.tag
                }
                findViewById<SwitchCompat>(R.id.filter_is_valid).apply {
                    isChecked = filter.valid
                    setOnCheckedChangeListener { _, isChecked ->
                        filter.valid = isChecked
                    }
                }
                putView(filter.key, this)
            }
        }
    }

    private fun findViewByKey(key: String) {
        viewModel.findViewByKey(key, viewBinding.root)
    }

    private fun putView(key: String, view: View) {
        viewModel.putView(key, view)
    }

}