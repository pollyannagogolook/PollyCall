package com.pollyanna.pollycall.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.pollyanna.pollycall.R
import com.pollyanna.pollycall.databinding.FragmentErrorDialogPageBinding

class ErrorDialogPage : DialogFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentErrorDialogPageBinding.inflate(inflater)
        return binding.root
    }
}