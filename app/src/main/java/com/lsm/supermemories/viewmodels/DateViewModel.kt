package com.lsm.supermemories.viewmodels

import androidx.lifecycle.asLiveData
import com.lsm.supermemories.R
import com.lsm.supermemories.data.Memory
import com.lsm.supermemories.data.Repository
import com.lsm.supermemories.ui.base.NavigEvent
import com.lsm.supermemories.ui.base.NavigateEventInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DateViewModel @Inject public constructor(
    repository: Repository): BaseViewModel()
{
        val dates = repository.getAllDates().asLiveData()
        fun chooseDate(memory: Memory){
                val navigInfo = NavigateEventInfo(
                        R.id.action_dateFragment_to_titleFragment,
                        arrayListOf(Pair("Date", memory.Date))
                )
                _navigateToFragment.value = NavigEvent(navigInfo)
        }
}