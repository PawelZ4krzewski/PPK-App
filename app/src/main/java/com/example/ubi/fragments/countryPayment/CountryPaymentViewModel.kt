package com.example.ubi.fragments.countryPayment

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.payment.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.text.DecimalFormat

class CountryPaymentViewModel(private val repository: PaymentRepository, application: Application) :
    AndroidViewModel(application) {
    private val _countryPayment = MutableStateFlow("")
    private val _date = MutableStateFlow("")
    private val _unitValue = MutableStateFlow("")


    val addPaymentToast = MutableStateFlow(false)

    val isAddPaymentEnable = combine(_countryPayment, _date) { countryPayment, date ->
        return@combine  countryPayment.isNotBlank() && date.isNotBlank()
    }

    val countryPayment
        get() = _countryPayment

    val date
        get() = _date

    val unitValue
        get() = _unitValue


    fun setCountryPayment(payment: String) {
        _countryPayment.value = payment
    }

    fun setDate(date: String) {
        _date.value = date
    }

    fun setUnitValue(unitValue: String) {
        _unitValue.value = unitValue
    }


    fun addPayment(userId: Int){

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val ppkAmount = df.format(countryPayment.value.toFloat() / unitValue.value.toFloat()).toFloat()
        val payment = Payment(0,userId, 0f, 0f, countryPayment.value.toFloat(), ppkAmount, date.value)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addPayment(payment)
                Log.d("EMP PAYMENT", "DODANO PAYMENT")
                resetValues()
                addPaymentToast.value = true
            }
            catch(e: Exception){
                Log.e("EMP PAYMENT", e.toString())
            }
        }
    }

    private fun resetValues(){
        _countryPayment.value = ""
        _date.value = ""
    }
}