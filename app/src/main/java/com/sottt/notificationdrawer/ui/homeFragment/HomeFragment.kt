package com.sottt.notificationdrawer.ui.homeFragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.MainActivity
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import com.sottt.notificationdrawer.data.defined.NotificationInfoAdapter
import com.sottt.notificationdrawer.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    companion object {
        const val TAG = "NotificationListener_SOTTT_HomeFragment"
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }

    private var _viewBinding: FragmentHomeBinding? = null

    private val viewBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Util.LogUtil.d(TAG, "HomeFragment onCreate")
    }

    override fun onResume() {
        super.onResume()
        Util.LogUtil.d(TAG, "HomeFragment onResume")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        Util.LogUtil.d(TAG, "HomeFragment onCreateView")
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startService()
        val activity = activity as MainActivity
        activity.bindListenerService()
        Util.LogUtil.d(TAG, "HomeFragment onViewCreated")
        initAdapterData()
        iniView()
    }

    override fun onDestroy() {
        super.onDestroy()
        Util.LogUtil.d(TAG, "HomeFragment onDestroy")
        _viewBinding = null
    }

    private fun iniView() {
        addNotificationListSyncs()
        iniListViewItemCallback()
    }

    private fun iniListViewItemCallback() {
        viewBinding.cardList.setOnItemClickListener { _, _, position, _ ->
            Util.LogUtil.d(TAG, "view clicked : position is $position")
            val item = viewModel.adapter.getItem(position)
            if (item != null) {
                val bundle = Bundle().apply {
                    putString("TITLE", item.title)
                    putString("CONTENT", item.title)
                    putString("TIME", item.time)
                    putString("PACKAGE_NAME", item.packageName)
                    putParcelable("ICON", item.smallIcon)
                }
                val activity = activity as MainActivity
                activity.showBottomSheetDialog(bundle)
            }
        }
    }

    private fun addNotificationListSyncs() {
        Repository.setOnActiveNotificationChanged(object : Repository.OnActiveNotificationChanged {
            override fun onActiveNotificationAdded(
                notification: NotificationInfo,
                category: Repository.NotificationCategory
            ) {
                viewModel.adapter.add(notification)
                viewModel.adapter.notifyDataSetChanged()
            }

            override fun onActiveNotificationRemoved(
                notification: NotificationInfo,
                category: Repository.NotificationCategory
            ) {
                viewModel.adapter.add(notification)
                viewModel.adapter.notifyDataSetChanged()
            }

        })
        viewBinding.cardList.adapter = viewModel.adapter
        viewModel.adapter.notifyDataSetChanged()
    }

    private fun initAdapterData() {
        if (viewModel.adapterInitFlag) {
            return
        }
        val list = mutableListOf<NotificationInfo>()
        viewModel.adapter =
            NotificationInfoAdapter(
                this.activity as MainActivity,
                R.layout.notification_card,
                list
            )
        viewModel.adapterInitFlag = true
    }

    private fun startService() {
        if (activity == null) {
            Util.LogUtil.w(TAG, "activity context is null!!")
        } else {
            val activity = activity as MainActivity
            activity.startListenerService()
        }
    }


}