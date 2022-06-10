package com.example.ubi.fragments.homeScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.Ppk
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.User
import com.example.ubi.database.user.UserRepository
import com.example.ubi.fragments.loginFragment.LoginFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.math.RoundingMode
import java.text.DecimalFormat

class HomeScreenViewModel(private val repository: PaymentRepository, application: Application, _user: User) :
    AndroidViewModel(application){

    val user: User = _user
    private var _ppk: Ppk? = null

    private val _stateOfFunds = MutableStateFlow("0")
    private val _totalPayment = MutableStateFlow("0")
    private val _ownPayment = MutableStateFlow("0")
    private val _empPayment = MutableStateFlow("0")
    private val _statePayment = MutableStateFlow("0")
    private val _inflationPayment = MutableStateFlow("0")

    private val _userPayments = MutableStateFlow(listOf<Payment>())

    val stateOfFunds get() = _stateOfFunds
    val totalPayment get() = _totalPayment
    val ownPayment get() = _ownPayment
    val empPayment get() = _empPayment
    val statePayment get() = _statePayment
    val inflationPayment get() = _inflationPayment

    val isPaymentGot = MutableStateFlow(false)

    val ppk get() = _ppk!!


    init {
//        getPpk()
        getPayments()
        Log.d("PPKVM", _userPayments.toString())
    }

    fun setPpk(ppk:Ppk){
        _ppk = ppk
    }

    fun getPayments(){
        isPaymentGot.value = false
        viewModelScope.launch{
            val payments = repository.getUserPayment(userId = user.userId)
            Log.d("Home Screen", payments.toString())
            if(payments != null){
                _userPayments.value = payments
                isPaymentGot.value = true
            }

        }
    }

    fun setValues(){

        stateOfFunds.value = "0"
        ownPayment.value = "0"
        empPayment.value = "0"
        statePayment.value = "0"

        _userPayments.value.forEach {
            stateOfFunds.value = (stateOfFunds.value.toFloat() + it.ppkAmount).toString()
            ownPayment.value = (ownPayment.value.toFloat() + it.userPayment).toString()
            empPayment.value = (empPayment.value.toFloat() + it.companyPayment).toString()
            statePayment.value = (statePayment.value.toFloat() + it.countryPayment).toString()
        }

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        val todayValue = ppk.values[ppk.values.size - 1].toFloat()
        val fullStateOfFunds = df.format(todayValue * stateOfFunds.value.toFloat())

        stateOfFunds.value = fullStateOfFunds.toString()

        totalPayment.value = (df.format(ownPayment.value.toFloat() + empPayment.value.toFloat() + statePayment.value.toFloat())).toString()

        ownPayment.value = df.format(ownPayment.value.toFloat()).toString()
        empPayment.value = df.format(empPayment.value.toFloat()).toString()
        statePayment.value = df.format(statePayment.value.toFloat()).toString()
    }

}