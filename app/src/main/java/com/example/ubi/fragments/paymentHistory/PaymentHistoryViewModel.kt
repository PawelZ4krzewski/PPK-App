package com.example.ubi.fragments.paymentHistory

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.UserPayment
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch

class PaymentHistoryViewModel(private val repository: PaymentRepository, application: Application) :
    AndroidViewModel(application) {

    private val payments = MutableStateFlow(listOf<Payment>())
    val userPayments = MutableStateFlow(mutableListOf<UserPayment>())
    val isLoading = MutableStateFlow(false)
    val isPaymentGot = MutableStateFlow(false)


    fun getPayments(userId: Int){

        viewModelScope.launch{
            isLoading.value = true
            try {
                val dbPayments = repository.getUserPayment(userId = userId)
                Log.d("PaymentHistory", dbPayments.toString())
                if(payments != null){
                    payments.value = dbPayments!!
                }
                Log.d("PaymentHistory", "Payments.value :${payments.value}")

                payments.value.forEach {
                    userPayments.value.add(UserPayment(it))
                }
                Log.d("PaymentHistory", "userPayments.value :${userPayments.value}")
                isPaymentGot.value = true
            }catch (e: Exception){
                Log.e("PaymentHistory", e.toString())
            }finally {
                isLoading.value = false

            }

        }
    }
}