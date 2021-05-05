package com.example.jetpacksample.common.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.PagingDataAdapter
import com.example.jetpacksample.R
import com.example.jetpacksample.databinding.ItemNetworkStateBinding
import com.example.jetpacksample.http.ApiException

class LoadStateAdapter(private val adapter: PagingDataAdapter<*, *>) : AbstractLoadStateAdapter<ItemNetworkStateBinding>() {
    override fun createBinding(parent: ViewGroup): ItemNetworkStateBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_network_state, parent, false)

    override fun bindState(binding: ItemNetworkStateBinding, loadState: LoadState) {
        binding.retryBtn.setOnClickListener { adapter.retry() }
        when (loadState) {
            is LoadState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
                binding.noMoreTv.visibility = View.INVISIBLE
                binding.retryLayout.visibility = View.INVISIBLE
            }
            is LoadState.Error -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.noMoreTv.visibility = View.INVISIBLE
                binding.retryLayout.visibility = View.VISIBLE
                binding.errorMsgTv.text = (loadState.error as ApiException).showMessage
            }
            else -> {
                binding.loadingLayout.visibility = View.INVISIBLE
                binding.noMoreTv.visibility = View.VISIBLE
                binding.retryLayout.visibility = View.INVISIBLE
            }
        }
    }
}
