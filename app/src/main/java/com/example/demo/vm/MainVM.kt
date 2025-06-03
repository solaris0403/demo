package com.example.demo.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huaxia.common.ext.launchOnIO

class MainVM: ViewModel() {
    private val _modeLiveData = MutableLiveData<Int>()

    public fun getMode() = _modeLiveData

    fun demo(){
        launchOnIO {
            _modeLiveData.postValue(1)
        }
    }
}