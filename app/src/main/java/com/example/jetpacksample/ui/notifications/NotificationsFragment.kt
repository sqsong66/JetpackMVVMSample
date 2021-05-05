package com.example.jetpacksample.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.jetpacksample.base.ui.BaseFragment
import com.example.jetpacksample.databinding.FragmentNotificationsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    private val notificationsViewModel: NotificationsViewModel by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentNotificationsBinding =
        FragmentNotificationsBinding.inflate(inflater, container, false)

    override fun initEvent() {
        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
    }

}