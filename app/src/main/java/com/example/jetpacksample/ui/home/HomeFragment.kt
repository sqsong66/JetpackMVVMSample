package com.example.jetpacksample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.jetpacksample.R
import com.example.jetpacksample.base.ui.BaseFragment
import com.example.jetpacksample.common.loading.LoadingState
import com.example.jetpacksample.common.loading.RetryCallback
import com.example.jetpacksample.common.recycler.LoadStateAdapter
import com.example.jetpacksample.databinding.FragmentHomeBinding
import com.example.jetpacksample.http.ApiException
import com.example.jetpacksample.ui.home.adapter.HomeArticleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var articleAdapter: HomeArticleAdapter

    private val homeViewModel: HomeViewModel by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding =
        FragmentHomeBinding.inflate(inflater, container, false)

    @FlowPreview
    override fun initEvent() {
        binding.viewModel = homeViewModel
        binding.swipeLayout.setColorSchemeResources(R.color.purple_200)
        binding.swipeLayout.setOnRefreshListener { articleAdapter.refresh() }
        binding.callback = object : RetryCallback {
            override fun retry(type: Int) {
                articleAdapter.retry()
            }
        }
        binding.recycler.adapter =
            articleAdapter.withLoadStateFooter(LoadStateAdapter(articleAdapter))

        setupState()
    }

    @FlowPreview
    private fun setupState() {
        lifecycleScope.launchWhenCreated {
            articleAdapter.loadStateFlow.collectLatest {
                binding.swipeLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }

        lifecycleScope.launchWhenCreated {
            homeViewModel.articleList.collectLatest {
                articleAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            articleAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.Error }
                .collect {
                    val error = (it.refresh as LoadState.Error).error as ApiException
                    if (error.code == ApiException.ERROR_EMPTY) {
                        homeViewModel.setLoadingState(LoadingState.emptyState())
                    } else {
                        homeViewModel.setLoadingState(LoadingState.errorState(error.showMessage))
                    }
                }
        }
        lifecycleScope.launchWhenCreated {
            articleAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { homeViewModel.setLoadingState(LoadingState.successState()) }
        }
    }

}