package com.pollyanna.pollycall.uploadnumber

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.pollyanna.pollycall.R
import com.pollyanna.pollycall.data.Call
import com.pollyanna.pollycall.data.CallResponse
import com.pollyanna.pollycall.iap.SubscriptionViewModel
import com.pollyanna.pollycall.databinding.FragmentUploadNumberPageBinding
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
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

        // collect connection state
        lifecycleScope.launch {
            subscriptionViewModel.billingConnectionState.collect { connectionState ->
                if (connectionState) {
                    binding.premiumBtn.visibility = View.VISIBLE
                } else {
                    binding.premiumBtn.visibility = View.INVISIBLE
                }
            }
        }


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

        // handle the subscription
        binding.premiumBtn.setOnClickListener {
            Log.i(IAP_TAG, "onCreateView: premium button clicked")
            subscriptionViewModel.buy(requireActivity())
        }

//        lifecycleScope.launch {
//            subscriptionViewModel.showLottie.collect { showLottie ->
//                if (showLottie) {
//                    binding.successAnimation.visibility = View.VISIBLE
//                    binding.successAnimation.playAnimation()
//                } else {
//                    binding.successAnimation.visibility = View.INVISIBLE
//                }
//            }
//        }


        return binding.root
    }


}