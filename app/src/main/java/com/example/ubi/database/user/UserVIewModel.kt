package com.example.ubi.database.user

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.PPKDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserVIewModel(application: Application): AndroidViewModel(application) {

    private val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = PPKDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }


}