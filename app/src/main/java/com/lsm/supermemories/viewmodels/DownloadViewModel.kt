package com.lsm.supermemories.viewmodels

import android.content.Context
import android.content.SharedPreferences
import com.lsm.supermemories.MainApplication
import com.lsm.supermemories.R
import com.lsm.supermemories.data.Memory
import com.lsm.supermemories.data.Repository
import com.lsm.supermemories.ui.base.NavigEvent
import com.lsm.supermemories.ui.base.NavigateEventInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import javax.inject.Inject


@HiltViewModel
class DownloadViewModel @Inject constructor(
        private val repository: Repository
) :  BaseViewModel() {


    fun add_new_memory(_text: String, _title: String,current_date: String,_imageUri: String) {
        val context = MainApplication.applicationContext()
        var sharedPreferences = context.getSharedPreferences("latitude", Context.MODE_PRIVATE)
        var sharedPreferences2 = context.getSharedPreferences("longitude", Context.MODE_PRIVATE)
        var latitude = sharedPreferences.getFloat("latitude", 0.0F)
        var longitude = sharedPreferences2.getFloat("longitude", 0.0F)
        var latitude2 = latitude.toDouble()
        var longitude2 = longitude.toDouble()
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("latitude").apply()
        editor.clear().apply()
        val editor2: SharedPreferences.Editor = sharedPreferences2.edit()
        editor2.remove("longitude").apply()
        editor2.clear().apply()
        val memoryData = Memory(0, _title, _text, current_date, _imageUri, latitude2, longitude2)
        GlobalScope.async {
            repository.memoryDao.insert(memoryData)
        }


            val navigInfo = NavigateEventInfo(
                    R.id.action_downloadMemory_to_memoryInfoFragment,
                    arrayListOf(Pair("title", _title)


                    ))
            _navigateToFragment.value = NavigEvent(navigInfo)

    }

    }

