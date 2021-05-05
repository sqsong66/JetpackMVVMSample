package com.example.jetpacksample.base.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.example.jetpacksample.utils.ext.setStatusBarTextColor

abstract class BaseActivity<V : ViewDataBinding> : AppCompatActivity() {

    protected lateinit var binding: V

    // init data binding.
    abstract fun initBinding(): V

    // init your something...
    abstract fun initEvent(savedInstanceState: Bundle?)

    // define the status bar text color(black or white).
    open fun isDarkStatusBarText(): Boolean = true

    open fun preInflateView() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        // Set status bar text color.
        setStatusBarTextColor(isDarkStatusBarText())
        preInflateView()
        super.onCreate(savedInstanceState)
        binding = initBinding()
        setContentView(binding.root)
        initEvent(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

}