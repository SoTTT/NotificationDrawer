package com.sottt.notificationdrawer.ui.statisticalFragment

import android.graphics.Color
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.sottt.notificationdrawer.Util
import com.sottt.notificationdrawer.dao.Repository
import com.sottt.notificationdrawer.data.defined.NotificationInfo
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread

class StatisticalFragmentViewModel() : ViewModel() {

    data class PackageData(val list: List<NotificationInfo>, val name: String, val count: Int) {

    }

    private val lock = ReentrantLock()

    private val _pieChartsDataSet = MutableLiveData<PieData>()

    val pieChartsDataSet = _pieChartsDataSet as LiveData<PieData>

    fun getDataFromDao(packageName: String): List<NotificationInfo> {
        val task = Repository.getNotificationRecordWithPackageName(packageName)
        val list = task.get()
        if (list.isEmpty()) {
            Util.showToast("notification record is empty", Toast.LENGTH_SHORT)
        }
        return list
    }

    fun loadPieChartsData() {
        iniPieChartsDataSet(Repository.loadAllNotificationRecord().get())
    }

    private fun iniPieChartsDataSet(list: List<NotificationInfo>) {
        thread {
            val packageNameAndCount = Repository.getPackageNameAndCount().get()
            val entries = ArrayList<PieEntry>().apply {
                val count = list.size
                for (index in packageNameAndCount.indices) {
                    add(
                        PieEntry(
                            packageNameAndCount[index].count!!.toFloat() / count.toFloat(),
                            packageNameAndCount[index].name
                        )
                    )
                }
            }
            val colors = ArrayList<Int>().apply {
                for (index in 0..packageNameAndCount.size) {
                    add(Color.GRAY)
                }
            }
            val dataSet = PieDataSet(entries, "Charts").apply {
                this.colors = colors
                sliceSpace = 1f
            }
            lock.lock()
            if (Util.isMainThread())
                _pieChartsDataSet.value = PieData(dataSet)
            else
                _pieChartsDataSet.postValue(PieData(dataSet))
            lock.unlock()
        }
    }

    fun getDataCountFromDao(): Int {
        return Repository.getNotificationCount().get()
    }
}