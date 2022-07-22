package com.lsm.supermemories.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DownloadMemoryInfo: ViewModel(){
    var _text = MutableLiveData<String>()
    val text : LiveData<String> = _text

    var _title = MutableLiveData<String>()
    val title : LiveData<String> = _title

    var _image = MutableLiveData<String>()
    val image : LiveData<String> = _image


}