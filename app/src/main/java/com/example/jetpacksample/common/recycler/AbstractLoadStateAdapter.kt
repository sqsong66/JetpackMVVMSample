package com.example.jetpacksample.common.recycler

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

abstract class AbstractLoadStateAdapter<V : ViewDataBinding> : LoadStateAdapter<DataBindingViewHolder<V>>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): DataBindingViewHolder<V> {
        return DataBindingViewHolder(createBinding(parent))
    }

    override fun onBindViewHolder(holder: DataBindingViewHolder<V>, loadState: LoadState) {
        bindState(holder.binding, loadState)
        holder.binding.executePendingBindings()
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return super.displayLoadStateAsItem(loadState) || (loadState is LoadState.NotLoading && loadState.endOfPaginationReached)
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    protected abstract fun bindState(binding: V, loadState: LoadState)
}