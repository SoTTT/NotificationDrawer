package com.sottt.notificationdrawer.ui.homeFragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.sottt.notificationdrawer.DAO.Repository
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

//    private val bottomSheetBehavior by lazy {
//        BottomSheetBehavior.from(viewBinding.bottomSheet)
//    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }

    private var _viewBinding: FragmentHomeBinding? = null

    private val viewBinding get() = _viewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startService()
        val activity = activity as MainActivity
        activity.bindListenerService()
        iniView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    private fun iniView() {
        addNotificationListSyncs()
        iniListViewItemCallback()
    }

    private fun iniListViewItemCallback() {
        viewBinding.cardList.setOnItemClickListener { _, _, position, _ ->
            Util.LogUtil.d(TAG, "view clicked : position is $position")
            val item = viewModel.adapterData.value?.elementAt(position)
            if (item != null) {
                val bundle = Bundle().apply {
                    putString("TITLE", item.title)
                    putString("CONTENT", item.title)
                    putString("TIME", item.time)
                    putString("PACKAGE_NAME", item.packageName)
                }
                val activity = activity as MainActivity
                activity.showBottomSheetDialog(bundle)
            }
        }
    }

    private fun addNotificationListSyncs() {
        Repository.activeNotification.observe(this.viewLifecycleOwner) {
            viewModel.setCurrentNotification(it)
        }
        val list = mutableListOf<NotificationInfo>()
        val adapter =
            NotificationInfoAdapter(
                this.activity as MainActivity,
                R.layout.notification_card,
                list
            )
        viewModel.adapterData.observe(this.viewLifecycleOwner) {
            Util.LogUtil.d(TAG, it.size.toString())
            for (item in it) {
                Util.LogUtil.d(TAG, item.toString())
            }
            adapter.clear()
            adapter.addAll(it)
        }
        viewBinding.cardList.adapter = adapter
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