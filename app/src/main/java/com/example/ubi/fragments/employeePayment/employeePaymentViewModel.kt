package com.example.ubi.fragments.employeePayment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ubi.database.payment.PaymentRepository
import kotlinx.coroutines.flow.MutableStateFlow

class employeePaymentViewModel(private val repository: PaymentRepository, application: Application) :
    AndroidViewModel(application) {

    private val _ownPayment = MutableStateFlow("")
    private val _empPayment = MutableStateFlow("")
    private val _date = MutableStateFlow("")
    private val _unitValue = MutableStateFlow("")

    val ownPayment
            get() = _ownPayment

    val empPayment
            get() = _empPayment

    val date
            get() = _date

    val unitValue
        get() = _unitValue

    fun setOwnPayment(payment: String) {
        _ownPayment.value = payment
    }

    fun setEmpPayment(payment: String) {
        _empPayment.value = payment
    }

    fun setDate(date: String) {
        _date.value = date
    }

    fun setUnitValue(unitValue: String) {
        _unitValue.value = unitValue
    }

}