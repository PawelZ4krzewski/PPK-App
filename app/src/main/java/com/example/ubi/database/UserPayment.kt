package com.example.ubi.database

import com.example.ubi.database.payment.Payment

data class UserPayment(
    val payment: Payment,
    var visibility: Boolean = false
)
