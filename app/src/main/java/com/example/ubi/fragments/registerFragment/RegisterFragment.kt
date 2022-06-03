package com.example.ubi.fragments.registerFragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ubi.R
import com.example.ubi.activities.LoginViewModel
import com.example.ubi.database.UserVIewModel
import com.example.ubi.databinding.FragmentRegisterBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class RegisterFragment : Fragment() {


    private val loginViewModel: LoginViewModel by activityViewModels()

    private val registerViewModel = RegisterViewModel()

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.usernameTextInputEditText.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setUsername(text.toString())
        }

        binding.passwordTextInputEditText.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setPassword(text.toString())
        }

        binding.repeatPasswordTextInputEditText.doOnTextChanged { text, _, _, _ ->
            registerViewModel.setRepeatPassword(text.toString())
        }

        collectFlow()

        binding.registrationButton.setOnClickListener {

            loginViewModel.setUsername(registerViewModel.getUsername())
            loginViewModel.setPassword(registerViewModel.getPassword())

            findNavController().navigate(R.id.action_registerFragment_to_companyChoose)
        }
    }

    private fun collectFlow() {
        lifecycleScope.launch {
            registerViewModel.isRegistrationEnable.collect {
                binding.registrationButton.isEnabled = it
            }
        }
    }

    private fun insertUserToDatabase() {
        val username = binding.usernameTextInputEditText.text?.trim().toString()
        val password = binding.passwordTextInputEditText.text.toString()
        val repeatPassword = binding.repeatPasswordTextInputEditText.text.toString()
    }

}