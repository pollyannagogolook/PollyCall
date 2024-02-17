package com.pollyanna.pollycall.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.pollyanna.pollycall.R
import com.pollyanna.pollycall.databinding.FragmentLoginBinding
import com.pollyanna.pollycall.iap.SubscriptionViewModel
import com.pollyanna.pollycall.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginPage : Fragment() {
    private val subscriptionViewModel by viewModels<SubscriptionViewModel>()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater)
        binding.viewModel = subscriptionViewModel

        // collect connection state
        lifecycleScope.launch {
            subscriptionViewModel.productPrice.collectLatest { productPrice ->
                if (productPrice?.isNotEmpty() == true) {
                    binding.premiumBtn.text = productPrice + "/ monthly"
                    binding.premiumBtn.visibility = View.VISIBLE
                }
            }
        }


        // collect available products to sale flow
        lifecycleScope.launch {
            subscriptionViewModel.buy(
                activity = requireActivity()
            )
        }


        // handle the subscription
        binding.premiumBtn.setOnClickListener {
            Log.i(Constants.IAP_TAG, "onCreateView: premium button clicked")
            subscriptionViewModel.buy(requireActivity())
        }

        // if the user is a subscription user, navigate to upload page
        lifecycleScope.launch {
            subscriptionViewModel.showPremiumFeatures.collect { showPremiumFeatures ->
                if (showPremiumFeatures) {
                    Log.i(Constants.IAP_TAG, "onCreateView: user is a subscription user")
                    // navigate to upload page
                    val navController = findNavController()
                    navController.navigate(LoginPageDirections.actionLoginPageToUploadNumberPage())
                }
            }
        }

        return binding.root
    }


}