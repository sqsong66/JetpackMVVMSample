package com.example.jetpacksample.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.example.jetpacksample.R
import com.example.jetpacksample.bean.ArticleItem
import com.example.jetpacksample.common.recycler.AbstractPagingAdapter
import com.example.jetpacksample.databinding.ItemHomeArticleBinding
import javax.inject.Inject

class HomeArticleAdapter @Inject constructor() :
    AbstractPagingAdapter<ArticleItem, ItemHomeArticleBinding>(object :
        DiffUtil.ItemCallback<ArticleItem>() {
        override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
            return oldItem.title == newItem.title
        }

    }) {
    override fun createBinding(parent: ViewGroup): ItemHomeArticleBinding =
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_article,
            parent,
            false
        )

    override fun bindData(binding: ItemHomeArticleBinding, data: ArticleItem?, position: Int) {
        binding.article = data
        binding.root.setOnClickListener {  }
    }
}