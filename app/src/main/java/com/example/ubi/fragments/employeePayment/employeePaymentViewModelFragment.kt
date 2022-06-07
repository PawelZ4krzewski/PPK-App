package com.example.ubi.fragments.employeePayment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.fragments.homeScreen.HomeScreenViewModel

class employeePaymentViewModelFragment(private  val repository: PaymentRepository,
private val application: Application
): ViewModelProvider.Factory{
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(employeePaymentViewModel::class.java)) {
            return employeePaymentViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}