package com.example.jetpacksample.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.jetpacksample.repository.IRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(repository: IRepository) : ViewModel() {

    @FlowPreview
    val articleList = flowOf(repository.getHomeArticles(20).cachedIn(viewModelScope)).flattenMerge(2)

}