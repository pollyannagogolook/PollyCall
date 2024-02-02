package com.example.pollycall.uploadnumber

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.pollycall.R
import com.example.pollycall.data.Call
import com.example.pollycall.data.CallResponse
import com.example.pollycall.data.iap.SubscriptionViewModel
import com.example.pollycall.databinding.FragmentUploadNumberPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [UploadNumberPage.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class UploadNumberPage : Fragment() {

    private val uploadNumberViewModel by viewModels<UploadNumberViewModel>()
    private val subscriptionViewModel by viewModels<SubscriptionViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentUploadNumberPageBinding.inflate(inflater)
        var stangeNumber = ""
        var numberOwner = ""
        var isScam = false


        // collect available products to sale flow
        lifecycleScope.launch {
            subscriptionViewModel.buy(
                activity = requireActivity()
            )
        }




        lifecycleScope.launch {
            uploadNumberViewModel.uploadResponseFlow.collect {

                when (it) {
                    is CallResponse.Loading -> {
                    }

                    is CallResponse.Success -> {
                        binding.successAnimation.visibility = View.VISIBLE
                        binding.successAnimation.playAnimation()

                        // clear the input
                        binding.phoneNumberTextInputEditText.text?.clear()
                        binding.phoneNumberOwnerInputEditText.text?.clear()
                        binding.isScamRadioGroup.clearCheck()
                    }

                    is CallResponse.Error -> {
                        binding.failAnimation.visibility = View.VISIBLE
                        binding.failAnimation.playAnimation()
                    }


                }
                delay(1500)
                binding.successAnimation.visibility = View.INVISIBLE
                binding.failAnimation.visibility = View.INVISIBLE
                binding.submitButton.isClickable = true
            }
        }

        binding.phoneNumberTextInputEditText.doAfterTextChanged {
            stangeNumber = binding.phoneNumberTextInputEditText.text.toString()
        }

        binding.phoneNumberOwnerInputEditText.doAfterTextChanged {
            numberOwner = binding.phoneNumberOwnerInputEditText.text.toString()
        }

        binding.isScamRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.is_scam_yes -> isScam = true
                R.id.is_scam_no -> isScam = false
            }

        }

        binding.submitButton.setOnClickListener {
            binding.submitButton.isClickable = false
            val submittedCall = Call(stangeNumber, numberOwner, isScam)
            uploadNumberViewModel.uploadNumber(submittedCall)
        }


        return binding.root
    }


}