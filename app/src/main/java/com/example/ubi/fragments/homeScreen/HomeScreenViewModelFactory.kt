package com.example.ubi.fragments.homeScreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ubi.database.payment.PaymentRepository

class HomeScreenViewModelFactory(
    private  val repository: PaymentRepository,
    private val application: Application
): ViewModelProvider.Factory{
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}