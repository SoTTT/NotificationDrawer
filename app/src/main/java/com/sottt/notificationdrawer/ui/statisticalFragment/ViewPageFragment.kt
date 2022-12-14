package com.sottt.notificationdrawer.ui.statisticalFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sottt.notificationdrawer.MainActivity
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import com.sottt.notificationdrawer.data.defined.NotificationInfoAdapter
import com.sottt.notificationdrawer.databinding.FragmentViewPageBinding
import com.sottt.notificationdrawer.ui.homeFragment.HomeFragment
import java.io.Serializable

class ViewPageFragment(
    private val viewModel: StatisticalFragmentViewModel,
    private val viewIndex: Int,
    private val mActivity: MainActivity
) : Fragment() {

    private var _viewBinding: FragmentViewPageBinding? = null

    private val viewBinding get() = _viewBinding!!

    private var list: ArrayList<NotificationInfo> = ArrayList()
        set(value) {
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getBundleData(arguments ?: Bundle())
    }

    private fun getBundleData(savedInstanceState: Bundle) {
        val arraySize = savedInstanceState.getInt("SIZE")
        if (arraySize != 0) {
            val array = ArrayList<NotificationInfo>()
            for (index in 0..arraySize) {
                val s = savedInstanceState.getSerializable("ARG${index}") as NotificationInfo?
                if (s != null)
                    array.add(s)
            }
            list = array
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentViewPageBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniList()
        iniListViewItemCallback()
    }

    private fun iniListViewItemCallback() {
        viewBinding.list.setOnItemClickListener { _, _, position, _ ->
            Util.LogUtil.d(HomeFragment.TAG, "view clicked : position is $position")
            val fragmentViewModel = viewModel
            val index = fragmentViewModel.classificationNotification.value?.keys?.toList()
            val key = index?.elementAt(viewIndex)
            val array = fragmentViewModel.classificationNotification.value?.get(key)
            val item = array?.elementAt(position)
            if (item != null) {
                val bundle = Bundle().apply {
                    putString("TITLE", item.title)
                    putString("CONTENT", item.title)
                    putString("TIME", item.time)
                    putString("PACKAGE_NAME", item.packageName)
                    putParcelable("ICON", item.smallIcon)
                }
                val activity = mActivity
                activity.showBottomSheetDialog(bundle)
            }
        }
    }

    private fun iniList() {
        val adapter =
            NotificationInfoAdapter(
                this.activity as Context,
                R.layout.statistical_notification_card,
                list
            )
        viewBinding.list.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

}