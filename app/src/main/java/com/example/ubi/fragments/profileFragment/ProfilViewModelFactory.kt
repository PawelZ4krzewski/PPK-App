package com.example.ubi.fragments.profileFragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.User
import com.example.ubi.fragments.homeScreen.HomeScreenViewModel

class ProfilViewModelFactory(
    private  val repository: PaymentRepository,
    private val application: Application,
    private val user: User
): ViewModelProvider.Factory{
    @Suppress("Unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            return HomeScreenViewModel(repository, application, user) as T
        }
        throw IllegalArgumentException("Unknown View Model Class")
    }
}