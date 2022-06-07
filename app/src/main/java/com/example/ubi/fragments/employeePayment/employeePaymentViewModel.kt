package com.example.ubi.fragments.employeePayment

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.payment.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.sql.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern

class employeePaymentViewModel(private val repository: PaymentRepository, application: Application) :
    AndroidViewModel(application) {

    private val _ownPayment = MutableStateFlow("")
    private val _empPayment = MutableStateFlow("")
    private val _date = MutableStateFlow("")

    val isAddPaymentEnable = combine(_ownPayment, _empPayment, _date) { ownPayment, empPayment, date ->
        return@combine  ownPayment.isNotBlank() && empPayment.isNotBlank() && date.isNotBlank()
    }

    val addPaymentToast = MutableStateFlow(false)

    val ownPayment
            get() = _ownPayment

    val empPayment
            get() = _empPayment

    val date
            get() = _date

    fun setOwnPayment(payment: String) {
        _ownPayment.value = payment
    }

    fun setEmpPayment(payment: String) {
        _empPayment.value = payment
    }

    fun setDate(date: String) {
        _date.value = date
    }


    fun addPayment(userId: Int, unitValue: String){

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN
        val ppkAmount = df.format((ownPayment.value.toFloat() + empPayment.value.toFloat()) / unitValue.toFloat()).toFloat()
        val payment = Payment(0,userId, ownPayment.value.toFloat(), empPayment.value.toFloat(), 0f, ppkAmount, date.value)

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
        _ownPayment.value = ""
        _empPayment.value = ""
        _date.value = ""
    }

}