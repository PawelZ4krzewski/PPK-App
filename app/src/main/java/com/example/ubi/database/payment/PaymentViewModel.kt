package com.example.ubi.database.payment

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.PPKDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentViewModel (application: Application): AndroidViewModel(application) {

    val readAllData: LiveData<List<Payment>>
    private val repository: PaymentRepository

    init {
        val paymentDao = PPKDatabase.getDatabase(application).PaymentDao()
        repository = PaymentRepository(paymentDao)
        readAllData = repository.readAllData
    }

    fun addPayment(payment: Payment){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPayment(payment)
        }
    }


}