package com.example.pollycall.uploadnumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.pollycall.R
import com.example.pollycall.databinding.FragmentLoginPageBinding

class LoginPage : Fragment() {


    private val viewModel: LoginPageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLoginPageBinding>(
            inflater,
            R.layout.fragment_login_page,
            container,
            false
        )

        binding.iapPurchaseBtn.setOnClickListener {
            // should take current purchase details
            // then purchase subscription
        }



        return binding.root
    }


}