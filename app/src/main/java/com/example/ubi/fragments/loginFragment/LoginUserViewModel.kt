package com.example.ubi.fragments.loginFragment

import android.app.Application
import android.database.Observable
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.room.ColumnInfo
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.User
import com.example.ubi.database.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest

class LoginUserViewModel(private val repository: UserRepository, application: Application) :
AndroidViewModel(application) {

    private val _username = MutableStateFlow("")
    private val _password = MutableStateFlow("")
    private val _isLoged = MutableLiveData<Boolean>()
    private val _isFailesLogin = MutableLiveData<Boolean>()
    val directionLiveData = MutableLiveData<NavDirections?>(null)


    fun setUsername(username: String){
        _username.value = username
    }
    fun setPassword(password: String){
        _password.value = password
    }

    fun login(username: String, password: String){
//        CoroutineScope(Dispatchers.Main + Job()).launch{
        viewModelScope.launch{
            val user = repository.getUser(username)

            if(user != null){
                printUserInformation(user)
                if(user.userPassword == md5(password)){
                    Log.d("LOGIN INF", "Wait Logging!")

//                    _isLoged.value = true
                    directionLiveData.value = LoginFragmentDirections.actionLoginFragmentToMainActivity()
                }
                else{
                    Log.d("LOGIN INF", "Incorrect Password: corect"+ user.userPassword + " your "+ md5(password))
                    _isFailesLogin.value = true
                }
            }
            else{
                _isFailesLogin.value = true
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


