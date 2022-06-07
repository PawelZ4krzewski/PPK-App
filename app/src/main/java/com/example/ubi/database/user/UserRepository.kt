package com.example.ubi.database.user

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    val readAllData:  LiveData<List<User>> = userDao.readAllData()

    suspend  fun addUser(user: User){
       userDao.addUser(user)
    }

    suspend fun getUser(username: String,): User? {
        return userDao.getUser(username)
    }

}

