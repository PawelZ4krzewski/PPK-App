package com.example.ubi.database.payment

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "payment_table")
data class Payment(
    @PrimaryKey(autoGenerate = true) val paymentId: Int,
    @NonNull @ColumnInfo(name = "userId") val userId: Int,
    @ColumnInfo(name = "userPayment") val userPayment: Float,
    @ColumnInfo(name = "companyPayment") val companyPayment: Float,
    @ColumnInfo(name = "countryPayment") val countryPayment: Float,
    @NonNull @ColumnInfo(name = "ppkAmount") val ppkAmount: Float,
    @ColumnInfo(name = "date") val date: String
)
