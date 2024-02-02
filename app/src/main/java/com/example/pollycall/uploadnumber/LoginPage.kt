package com.example.pollycall.uploadnumber

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.pollycall.R
import com.example.pollycall.databinding.FragmentLoginPageBinding

class LoginPage : Fragment() {


    private lateinit var viewModel: LoginPageViewModel

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



        return binding.root
    }


}