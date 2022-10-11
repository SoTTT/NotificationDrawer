package com.sottt.notificationdrawer.setting.ui.createFilterFragment

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
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
    private lateinit var selectedAppListView: ListView

    var tag: Int = TAG_DEFAULT_FILTER

    private var filterName = ""
    private var packageNameSet = HashSet<String>()

    private val selectedAppPackageName get() = packageNameSet.toList()
    private val selectedAppLabelName
        get() = selectedAppPackageName.toList().map {
            NotificationDrawerApplication.getAppName(it)
        }

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
                createPackageFilterView(inflater)
            }
        }
        return view
    }

    private fun createPackageFilterView(inflater: LayoutInflater) {
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
        selectedAppListView = view.findViewById<ListView>(R.id.target_app_list).apply {
            val adapter = ArrayAdapter<String>(
                this@CreateFilterFragment.activity as Context,
                android.R.layout.simple_list_item_1,
                selectedAppLabelName
            )
            this.adapter = adapter
        }
    }

    private fun createAppSelectDialog(): AlertDialog {
        val packageNameList = NotificationDrawerApplication.getInstalledAppList()
        val appList = packageNameList.map {
            NotificationDrawerApplication.getAppName(it)!!
        }.toTypedArray()
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
                for ((index, item) in packageNameList.withIndex()) {
                    if (checkedList.elementAt(index)) {
                        packageNameSet.add(item)
                    }
                }
                val adapter = ArrayAdapter<String>(
                    this@CreateFilterFragment.activity as Context,
                    android.R.layout.simple_list_item_1,
                    selectedAppLabelName
                )
                selectedAppListView.adapter = adapter
                dialog.dismiss()
            }.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    //do nothing
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    //do nothing
                }

            }).create()
        return dialog
    }

    private fun showAppSelectDialog() {
        createAppSelectDialog().show()
    }
}