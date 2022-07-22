package com.lsm.supermemories.viewmodels

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lsm.supermemories.MainApplication
import com.lsm.supermemories.R
import com.lsm.supermemories.data.Memory
import com.lsm.supermemories.data.Repository
import com.lsm.supermemories.ui.base.NavigEvent
import com.lsm.supermemories.ui.base.NavigateEventInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class MemoryInfoViewModel @AssistedInject constructor (
        private val repository: Repository,



@Assisted private val title: String
        ): BaseViewModel() {


    companion object {

        fun provideFactory(
                assistedFactory: MemoryInfoViewModelFactory,
                title: String

        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(title) as T
            }
        }

    }

    var buffer: MutableLiveData<String> = MutableLiveData()
    var infoTitle: MutableLiveData<String> = MutableLiveData()
    var infoContent: MutableLiveData<String> = MutableLiveData()
    var infoDate: MutableLiveData<String> = MutableLiveData()
    var decode_image: MutableLiveData<Bitmap> = MutableLiveData()
    private fun initMemory(): Flow<List<Memory>> {

        return repository.memoryDao.getMemory(title)


    }

    fun show_memory(): String? {


        viewModelScope.async {

            initMemory().collect() { memoryInfo ->
                memoryInfo.forEach { c ->
                    infoTitle.value = "Title: " + c.title
                    infoContent.value = "Content: " + c.memoryText
                    infoDate.value = "Date: " + c.Date
                    decode_image.value = BitmapFactory.decodeFile(c.memoryImage)

                    val context = MainApplication.applicationContext()
                    var sharedPreferences = context.getSharedPreferences("Image", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.remove("Image").clear().apply()

                    editor.putString("Image", c.memoryImage).apply()

                }


            }

        }

        return buffer.value

    }


    fun delete_memory() {
        viewModelScope.async {
            initMemory().collect() { deleteMemory ->
                deleteMemory.forEach { c ->
                    var record = Memory(c.uid, c.title, c.memoryText, c.Date, c.memoryImage, c.latitude, c.longitude)
                    GlobalScope.async {
                        repository.memoryDao.delete(record)
                    }
                }
            }
        }
        val navigInfo = NavigateEventInfo(
                R.id.action_memoryInfoFragment_to_homePage,


                )
        _navigateToFragment.value = NavigEvent(navigInfo)
    }

    fun go_to_map() {
        viewModelScope.async {
            initMemory().collect() { memoryInfo ->
                memoryInfo.forEach { c ->
                    val context = MainApplication.applicationContext()
                    var sharedPreferences = context.getSharedPreferences("latitude", Context.MODE_PRIVATE)
                    var sharedPreferences2 = context.getSharedPreferences("longitude", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    val editor2: SharedPreferences.Editor = sharedPreferences2.edit()
                    editor.remove("latitude").clear().apply()
                    editor2.remove("longitude").clear().apply()
                    editor.putFloat("latitude", c.latitude.toFloat()).apply()
                    editor2.putFloat("longitude", c.longitude.toFloat()).apply()
                    if (c.latitude == 0.0 && c.longitude == 0.0) {
                        Toast.makeText(MainApplication.applicationContext(), "Brak danych", Toast.LENGTH_LONG).show()
                    } else {
                        val navigInfo = NavigateEventInfo(
                                R.id.action_memoryInfoFragment_to_mapsFragment
                        )
                        _navigateToFragment.value = NavigEvent(navigInfo)
                    }
                }
            }
        }

    }
}

@AssistedFactory
interface MemoryInfoViewModelFactory {
    fun create(title: String) : MemoryInfoViewModel


}