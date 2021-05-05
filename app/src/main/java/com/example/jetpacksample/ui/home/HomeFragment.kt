package com.example.jetpacksample.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.jetpacksample.R
import com.example.jetpacksample.base.ui.BaseFragment
import com.example.jetpacksample.common.recycler.LoadStateAdapter
import com.example.jetpacksample.databinding.FragmentHomeBinding
import com.example.jetpacksample.ui.home.adapter.HomeArticleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
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
        binding.swipeLayout.setColorSchemeResources(R.color.purple_200)
        binding.swipeLayout.setOnRefreshListener { articleAdapter.refresh() }
        binding.recycler.adapter = articleAdapter.withLoadStateFooter(LoadStateAdapter(articleAdapter))

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
    }

}