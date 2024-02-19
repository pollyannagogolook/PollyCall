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
import com.pollyanna.pollycall.databinding.FragmentLoginBinding
import com.pollyanna.pollycall.utils.Constants
import com.pollyanna.pollycall.utils.Constants.Companion.IAP_TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.log


@AndroidEntryPoint
class LoginPage : Fragment() {
    private val loginViewModel by viewModels<LoginViewModel>()
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLoginBinding.inflate(inflater)
        binding.viewModel = loginViewModel



        // collect connection state
        lifecycleScope.launch {
            loginViewModel.productPrice.collectLatest { productPrice ->
                if (productPrice?.isNotEmpty() == true) {
                    binding.premiumBtn.text = productPrice + "/ monthly"
                    binding.premiumBtn.visibility = View.VISIBLE
                }
            }
        }


        // collect available products to sale flow
        lifecycleScope.launch {
            loginViewModel.buy(
                activity = requireActivity()
            )
        }


        // handle the subscription
        binding.premiumBtn.setOnClickListener {
            Log.i(Constants.IAP_TAG, "onCreateView: premium button clicked")
            loginViewModel.buy(requireActivity())
        }

        // if the user is a subscription user, navigate to upload page
        lifecycleScope.launch {
            loginViewModel.showPremiumFeatures.collect { showPremiumFeatures ->
                if (showPremiumFeatures) {
                    Log.i(Constants.IAP_TAG, "onCreateView: user is a subscription user")
                    // navigate to upload page
                    val navController = findNavController()
                    navController.navigate(LoginPageDirections.actionLoginPageToUploadNumberPage())
                }
            }
        }

        // if there is an error, navigate the error dialog
        lifecycleScope.launch{
            loginViewModel.errorText.collect{errorMessage ->
                    val action = errorMessage?.let {
                        LoginPageDirections.actionLoginPageToErrorDialogPage(
                            it
                        )
                    }
                if (action != null) {
                    findNavController().navigate(action)
                    loginViewModel.terminateBillingConnection()
                }
                    loginViewModel.removeErrorMsg()
                }


        }

        return binding.root
    }


}