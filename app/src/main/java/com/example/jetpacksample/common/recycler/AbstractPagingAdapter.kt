package com.example.jetpacksample.common.recycler

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class AbstractPagingAdapter<T : Any, V : ViewDataBinding>(diffCallback: DiffUtil.ItemCallback<T>) :
        PagingDataAdapter<T, DataBindingViewHolder<V>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder<V> =
            DataBindingViewHolder(createBinding(parent))

    override fun onBindViewHolder(holder: DataBindingViewHolder<V>, position: Int) {
        bindData(holder.binding, getItem(position), position)
        holder.binding.executePendingBindings()
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    protected abstract fun bindData(binding: V, data: T?, position: Int)
}