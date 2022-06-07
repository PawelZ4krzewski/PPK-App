package com.example.ubi.database.payment
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PaymentDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPayment(payment: Payment)

    @Query("SELECT * FROM payment_table ORDER BY userId ASC")
    fun readAllData(): LiveData<List<Payment>>

    @Query("SELECT * FROM payment_table WHERE userId LIKE :userId ORDER BY date ASC")
    suspend fun getUserPayment(userId: Int): List<Payment>?
}