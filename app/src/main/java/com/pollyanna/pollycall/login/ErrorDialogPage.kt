package com.pollyanna.pollycall.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.pollyanna.pollycall.R
import com.pollyanna.pollycall.databinding.FragmentErrorDialogPageBinding
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorDialogPage : DialogFragment() {

    private val args : ErrorDialogPageArgs by navArgs()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentErrorDialogPageBinding.inflate(inflater)
        val errorMsg = args.errorMsg

        binding.errorTitle.text = errorMsg.title
        binding.errorDetails.text = errorMsg.message


        binding.confirmBtn.setOnClickListener {
            findNavController().popBackStack()
        }


        return binding.root
    }
}