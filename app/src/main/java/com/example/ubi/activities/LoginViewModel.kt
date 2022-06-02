package com.example.ubi.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class LoginViewModel: ViewModel() {

    private var _username:String? = null
    private var _password: String? = null
    private var _repeatPassword: String? = null
    private var _additionalPercentage: String? = null
    private var _companyName: String? = null
    private var _additionalCompanyPercentage: String? = null
    private var _ppkName: String? = null

    private val username get() = _username
    private val password get() = _password
    private val repeatPassword get() = _repeatPassword
    private val additionalPercentage get() = _additionalPercentage
    private val companyName get() = _companyName
    private val additionalCompanyPercentage get()  = _additionalCompanyPercentage
    private val ppkName get() = _ppkName

    fun setUsername(username: String) {
        _username = username
    }

    fun setPassword(password: String) {
        _password = password
    }

    fun setRepeatPassword(password: String) {
        _repeatPassword = password
    }

    fun setAdditionalPercentage(value: String) {
        _additionalPercentage = value
    }

    fun setCompanyName(name: String) {
        _companyName = name
    }

    fun setAdditionalCompanyPercentage(value: String) {
        _additionalCompanyPercentage = value
    }

    fun setPpkName(ppk: String) {
        _ppkName = ppk
    }

    fun printRegistrationInfo(){
        Log.d("REGISTRATION","Username" + username)
        Log.d("REGISTRATION","Password" + password)
        Log.d("REGISTRATION","Repeat Password" + repeatPassword)
        Log.d("REGISTRATION","Add Per" + additionalPercentage)
        Log.d("REGISTRATION","Company Name" + companyName)
        Log.d("REGISTRATION","Add Comp Per" + additionalCompanyPercentage)
        Log.d("REGISTRATION","PPK Name" + ppkName)
    }
}