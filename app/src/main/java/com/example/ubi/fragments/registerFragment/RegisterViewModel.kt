package com.example.ubi.fragments.registerFragment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserRepository, application: Application) :
    AndroidViewModel(application) {
    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _repeatPassword = MutableStateFlow("")

    val isFreeUsername = MutableStateFlow(false)

    val isPasswordsSame  = combine(_password, _repeatPassword) { password, repeatPassword ->
        if(repeatPassword.isNotBlank())
            return@combine password == repeatPassword
        else
            return@combine true
    }

    val isRegistrationEnable = combine(_username, _password, _repeatPassword) { username, password, repeatPassword ->
        isUsernameFree(username)
        return@combine  username.isNotBlank() && password.isNotBlank() && repeatPassword.isNotBlank() && (password == repeatPassword)
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


    fun isUsernameFree(username: String){
        viewModelScope.launch{
            val user = repository.getUser(username)
            isFreeUsername.value = user == null
        }
    }


}