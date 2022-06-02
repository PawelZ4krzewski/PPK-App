package com.example.ubi.fragments.registerFragment

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class RegisterViewModel: ViewModel() {
    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _repeatPassword = MutableStateFlow("")




    val isRegistrationEnable = combine(_username, _password, _repeatPassword) { username, password, repeatPassword ->
        return@combine username.isNotBlank() && password.isNotBlank() && repeatPassword.isNotBlank() && (password == repeatPassword)
    }


    fun setUsername(username: String) {
        _username.value = username
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setRepeatPassword(password: String) {
        _repeatPassword.value = password
    }



    fun getUsername(): String {
        return _username.value
    }

    fun getPassword(): String {
        return _password.value
    }





}