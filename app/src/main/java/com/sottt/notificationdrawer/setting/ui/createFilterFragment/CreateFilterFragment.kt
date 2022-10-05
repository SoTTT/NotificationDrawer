package com.sottt.notificationdrawer.setting.ui.createFilterFragment

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.widget.addTextChangedListener
import com.sottt.notificationdrawer.NotificationDrawerApplication
import com.sottt.notificationdrawer.R
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.filter.PackageFilter
import com.sottt.notificationdrawer.setting.SettingActivity


class CreateFilterFragment : Fragment() {

    class ErrorTagException() : Exception() {
        override val message: String
            get() = "a valid filter tag can not be TAG_DEFAULT_FILTER"
    }

    private lateinit var rootLayout: FrameLayout

    var tag: Int = TAG_DEFAULT_FILTER

    private var filterName = ""
    private val packageNameSet = HashSet<String>()

    companion object {
        const val TAG_DEFAULT_FILTER = 0
        const val TAG_PACKAGE_FILTER = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val argTag = arguments?.getInt("TAG")
        if (argTag != null) {
            tag = argTag
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_create_filter, container, false)
        rootLayout = view as FrameLayout
        when (tag) {
            TAG_DEFAULT_FILTER -> throw ErrorTagException()
            TAG_PACKAGE_FILTER -> {
                val view = inflater.inflate(R.layout.create_package_filter_layout, rootLayout, true)
                view.findViewById<EditText>(R.id.editText)
                    .addTextChangedListener(object : TextWatcher {
                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            filterName = s?.toString() ?: ""
                        }

                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {
                            Util.LogUtil.v("CreateFilterFragment", "beforeTextChanged")
                        }

                        override fun afterTextChanged(s: Editable?) {
                            Util.LogUtil.v("CreateFilterFragment", "afterTextChanged")
                        }
                    })
                view.findViewById<Button>(R.id.add_app_button).setOnClickListener {
                    showAppSelectDialog()
                }
                view.findViewById<Button>(R.id.enter_button).setOnClickListener {
                    val activity = activity as SettingActivity
                    Repository.addFilter(PackageFilter().apply {
                        name = filterName
                        addPackageName(packageNameSet)
                    })
                    activity.navController.popBackStack()
                }
            }
        }
        return view
    }

    private fun createAppSelectDialog(): AlertDialog {
        val appList = NotificationDrawerApplication.getInstalledAppList().toTypedArray()
        val checkedList = List(appList.size) { index ->
            packageNameSet.contains(appList.elementAt(index))
        }.toBooleanArray();
        val dialog = AlertDialog.Builder(this.activity)
            .setTitle("select the application package name you want to filter")
            .setMultiChoiceItems(appList, checkedList) { _, which, isChecked ->
                checkedList[which] = isChecked
            }
            .setNegativeButton("cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("enter") { dialog, _ ->
                for ((index, item) in appList.withIndex()) {
                    if (checkedList.elementAt(index)) {
                        packageNameSet.add(item)
                    }
                }
                dialog.dismiss()
            }.create()
        return dialog
    }

    private fun showAppSelectDialog() {
        createAppSelectDialog().show()
    }
}