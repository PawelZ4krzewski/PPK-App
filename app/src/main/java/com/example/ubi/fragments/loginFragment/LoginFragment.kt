package com.example.ubi.fragments.loginFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.ubi.R
import com.example.ubi.databinding.FragmentLoginBinding
import java.util.Objects.isNull


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        setupLoginClick()
    }

    private fun setupLoginClick(){

        binding.loginButton.setOnClickListener {
            val usernameLayout = binding.usernameTextInputLayout
            val passwordLayout = binding.passwordTextInputLayout

            val username = binding.usernameTextInputEditText.text?.trim().toString()
            val password = binding.passwordTextInputEditText.text?.trim().toString()

            if(username.isBlank()){
                usernameLayout.error = "Username cannot be empty"
            }
            else{
                usernameLayout.error = ""
            }

            if(password.isBlank()){
                passwordLayout.error = "Password cannot be empty"
            }
            else{
                passwordLayout.error = ""
            }
        }
    }
}