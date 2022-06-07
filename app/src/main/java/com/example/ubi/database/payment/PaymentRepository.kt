package com.example.ubi.database.payment

import androidx.lifecycle.LiveData

class PaymentRepository (private val paymentDao: PaymentDao) {

    val readAllData: LiveData<List<Payment>> = paymentDao.readAllData()

    suspend fun addPayment(payment: Payment) {
        paymentDao.addPayment(payment)
    }

    suspend fun getUserPayment(userId: Int ): List<Payment>? {
        return paymentDao.getUserPayment(userId)
    }

}