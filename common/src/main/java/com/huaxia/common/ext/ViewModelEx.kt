package com.huaxia.common.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

inline fun ViewModel.launch(context: CoroutineContext = Dispatchers.Default, crossinline block: suspend () -> Unit): Job {
    return viewModelScope.launch(context) {
        block()
    }
}

inline fun ViewModel.launchOnMain(crossinline block: suspend () -> Unit): Job {
    return viewModelScope.launch(Dispatchers.Main) {
        block()
    }
}

inline fun ViewModel.launchOnIO(crossinline block: suspend () -> Unit): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        block()
    }
}