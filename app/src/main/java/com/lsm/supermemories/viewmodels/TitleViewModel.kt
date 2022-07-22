package com.lsm.supermemories.viewmodels

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.lsm.supermemories.MainApplication
import com.lsm.supermemories.R
import com.lsm.supermemories.data.Memory
import com.lsm.supermemories.data.Repository
import com.lsm.supermemories.ui.base.NavigEvent
import com.lsm.supermemories.ui.base.NavigateEventInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class TitleViewModel @AssistedInject constructor(
       repository: Repository,
       @Assisted private val Date: String
) : BaseViewModel() {
    val titles = repository.getAllTitles(Date).asLiveData()
    companion object {
        fun provideFactory(
                assistedFactory: TitleListViewModelFactory,
                Date: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(Date) as T
            }
        }
    }


    fun chooseTitle(memory: Memory) {
       val navigInfo = NavigateEventInfo(
                R.id.action_titleFragment_to_memoryInfoFragment,
                arrayListOf(Pair("title", memory.title)

               ))
        val context = MainApplication.applicationContext()
        var sharedPreferences = context.getSharedPreferences("Image", Context.MODE_PRIVATE)

        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("Image").clear().apply()

        editor.putString("Image", memory.memoryImage).apply()
        _navigateToFragment.value = NavigEvent(navigInfo)
    }
}
@AssistedFactory
interface TitleListViewModelFactory {
    fun create(Date: String) : TitleViewModel

}


