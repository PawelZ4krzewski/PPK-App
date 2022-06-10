package com.example.ubi.fragments.loginFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.ubi.R
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.user.User
import com.example.ubi.database.user.UserRepository
import com.example.ubi.database.user.UserVIewModel
import com.example.ubi.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var mUserViewModel: UserVIewModel



    private val loginUserViewModel by lazy {
        val application = requireNotNull(this.activity).application

        val dao = PPKDatabase.getDatabase(application).userDao()

        val repository = UserRepository(dao)

        LoginUserViewModel(repository,application)
    }


    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserVIewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.usernameTextInputEditText.doOnTextChanged { text, _, _, _ ->
            loginUserViewModel.setUsername(text.toString())
        }

        binding.passwordTextInputEditText.doOnTextChanged { text, _, _, _ ->
            loginUserViewModel.setPassword(text.toString())
        }


        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        setupLoginClick()

        collectFlow()

    }

    private fun setupLoginClick(){

        binding.loginButton.setOnClickListener {

            val username = binding.usernameTextInputEditText.text?.trim().toString()
            val password = binding.passwordTextInputEditText.text?.trim().toString()

            loginUserViewModel.login(username,password)

        }
    }

    private fun collectFlow() {

        loginUserViewModel.directionLiveData.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("Collect Flow", it.toString())
                findNavController().navigate(it)
//                if(it == LoginFragmentDirections.actionLoginFragmentToMainActivity()) {
//                    requireActivity().finish()
            }
        }

        lifecycleScope.launch {
            loginUserViewModel.isLoginAvailable.collect {
                binding.loginButton.isEnabled = it
            }
        }

        lifecycleScope.launch {
            loginUserViewModel.isIncorrectUsername.collect {
                if(it){
                    binding.usernameTextInputLayout.error = "Username is incorrect."
                }
                else{
                    binding.usernameTextInputLayout.error = ""
                }
            }
        }

        lifecycleScope.launch {
            loginUserViewModel.isIncorrectPassword.collect {
                if(it){
                    binding.passwordTextInputLayout.error = "Password is incorrect."
                }
                else{
                    binding.passwordTextInputLayout.error = ""
                }
            }
        }
    }
}