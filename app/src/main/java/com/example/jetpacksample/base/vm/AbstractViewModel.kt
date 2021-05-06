package com.example.jetpacksample.base.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.jetpacksample.common.loading.LoadingState

abstract class AbstractViewModel : ViewModel() {

    val loadingState by lazy { ObservableField(LoadingState.successState()) }

    fun setLoadingState(state: LoadingState) {
        loadingState.set(state)
    }

}