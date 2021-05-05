package com.example.jetpacksample.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<V: ViewDataBinding>: Fragment() {

    protected lateinit var binding: V

    // init data binding.
    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): V

    // init your something...
    abstract fun initEvent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = initBinding(inflater, container, savedInstanceState)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEvent()
    }

}