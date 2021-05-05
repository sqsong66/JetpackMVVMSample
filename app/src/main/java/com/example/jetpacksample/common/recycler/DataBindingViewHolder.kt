package com.example.jetpacksample.common.recycler

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DataBindingViewHolder<out V : ViewDataBinding> constructor(val binding: V) :
        RecyclerView.ViewHolder(binding.root)