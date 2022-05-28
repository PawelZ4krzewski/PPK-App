package com.example.ubi.fragments.registerFragment

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.ubi.R
import com.example.ubi.database.UserVIewModel
import com.example.ubi.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var mUserViewModel: UserVIewModel

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserVIewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.registrationButton.setOnClickListener {
            insertUserToDatabase()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun insertUserToDatabase() {
        val username = binding.usernameTextInputEditText.text?.trim().toString()
        val password = binding.passwordTextInputEditText.text.toString()
        val repeatPassword = binding.repeatPasswordTextInputEditText.text.toString()

        if(inputCheck(username, password, repeatPassword)){

        }
    }

    private fun inputCheck(username: String, password: String, repeatPassword: String): Boolean{
        return !(TextUtils.isEmpty(username) && TextUtils.isEmpty(password) && TextUtils.isEmpty(repeatPassword)) || (password == repeatPassword)
    }
}