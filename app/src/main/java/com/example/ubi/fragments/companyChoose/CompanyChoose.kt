package com.example.ubi.fragments.companyChoose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ubi.R
import com.example.ubi.activities.LoginViewModel
import com.example.ubi.databinding.FragmentCompanyChooseBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CompanyChoose : Fragment() {

    private val companyChooseViewModel: CompanyChooseViewModel  by viewModels()

    private val loginViewModel: LoginViewModel by activityViewModels()


    private var _binding: FragmentCompanyChooseBinding? = null
    private val binding get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         _binding = FragmentCompanyChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        companyChooseViewModel.setAdditionalPercentage(binding.extEmpPerTextInputEditText.text.toString())
        binding.extEmpPerTextInputEditText.doOnTextChanged{text,_,_,_ ->
            companyChooseViewModel.setAdditionalPercentage(text.toString())
            if(text.toString().isNotBlank()){
                if(0f>text.toString().toFloat()){
                    binding.countryPaymentTextInputLayout.error = "Extra employee payment must be bigger than 0"
                }
                else if(text.toString().toFloat()>2f){
                    binding.countryPaymentTextInputLayout.error = "Extra employee payment must be smaller than 2"
                }
                else{
                    binding.countryPaymentTextInputLayout.error = ""
                }
            }
        }



        binding.companyNameTextInputEditText.doOnTextChanged{text,_,_,_ ->
            companyChooseViewModel.setCompanyName(text.toString())
        }
        companyChooseViewModel.setAdditionalCompanyPercentage(binding.extCompPerInputEditText.text.toString())
        binding.extCompPerInputEditText.doOnTextChanged{text,_,_,_ ->
            companyChooseViewModel.setAdditionalCompanyPercentage(text.toString())
            if(text.toString().isNotBlank()) {
                if (0f > text.toString().toFloat()) {
                    binding.extCompPerInputLayout.error =
                        "Extra employee payment must be bigger than 0"
                } else if (text.toString().toFloat() > 2.5f) {
                    binding.extCompPerInputLayout.error =
                        "Extra employee payment must be smaller than 2.5"
                } else {
                    binding.extCompPerInputLayout.error = ""
                }
            }
        }

        collectFlow()

        binding.addExtInfoButton.setOnClickListener {
            loginViewModel.setAdditionalPercentage(companyChooseViewModel.getAdditionalPercentage())
            loginViewModel.setCompanyName(companyChooseViewModel.getCompanyName())
            loginViewModel.setAdditionalCompanyPercentage(companyChooseViewModel.getAdditionalCompanyPercentage())
            findNavController().navigate(R.id.action_companyChoose_to_ppkChoose)
        }

    }

    private fun collectFlow() {
        lifecycleScope.launch {
            companyChooseViewModel.isAdditionalEnable.collect {
                binding.addExtInfoButton.isEnabled = it
            }
        }
    }

}