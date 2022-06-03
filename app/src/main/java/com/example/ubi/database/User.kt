package com.example.ubi.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    @NonNull @ColumnInfo(name = "userName") val userName: String,
    @NonNull @ColumnInfo(name = "userPassword") val userPassword: String,
    @NonNull @ColumnInfo(name = "userPercentage") val userPercentage: Float,
    @NonNull @ColumnInfo(name = "CompanyName") val companyName: String,
    @NonNull @ColumnInfo(name = "companyPercentage") val companyPercentage: Float,
    @NonNull @ColumnInfo(name = "ppkId") val ppkId: String,
    @NonNull @ColumnInfo(name = "ppkName") val ppkName: String
)
