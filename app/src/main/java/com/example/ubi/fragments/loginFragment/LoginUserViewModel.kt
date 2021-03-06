package com.example.ubi.fragments.loginFragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import com.example.ubi.database.user.User
import com.example.ubi.database.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class LoginUserViewModel(private val repository: UserRepository, application: Application) :
AndroidViewModel(application) {

    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    val directionLiveData = MutableLiveData<NavDirections?>(null)
    var _user: User? = null

    val isIncorrectUsername = MutableStateFlow(false)
    val isIncorrectPassword = MutableStateFlow(false)

    val isLoginAvailable = combine(_username, _password){ username, password->
        return@combine username.isNotBlank() && password.isNotBlank()
    }

    fun setUsername(username: String){
        _username.value = username
    }
    fun setPassword(password: String){
        _password.value = password
    }

    fun login(username: String, password: String){

        isIncorrectUsername.value = false
        isIncorrectPassword.value = false

        viewModelScope.launch{
            val user = repository.getUser(username)

            if(user != null){
                printUserInformation(user)
                if(user.userPassword == md5(password)){
                    Log.d("LOGIN INF", "Wait Logging!")

//                    _isLoged.value = true
                    _user = user!!
                    directionLiveData.value = LoginFragmentDirections.actionLoginFragmentToMainActivity(user)
                }
                else{
                    Log.d("LOGIN INF", "Incorrect Password: corect"+ user.userPassword + " your "+ md5(password))
                    isIncorrectPassword.value = true
                }
            }
            else{
                isIncorrectUsername.value = true
            }
        }
    }

    private fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }

    private fun printUserInformation(user: User){
        Log.d("LOGIN INF", "ID: "+user.userId)
        Log.d("LOGIN INF", "Name: "+user.userName)
        Log.d("LOGIN INF", "Password: "+user.userPassword)
        Log.d("LOGIN INF", "Perc: "+user.userPercentage)
        Log.d("LOGIN INF", "Company: "+user.companyName)
        Log.d("LOGIN INF", "Company Perc: "+user.companyPercentage)
        Log.d("LOGIN INF", "PPK Id: "+user.ppkId)
        Log.d("LOGIN INF", "PPK Name: "+user.ppkName)
    }
}


