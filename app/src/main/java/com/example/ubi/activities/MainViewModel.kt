package com.example.ubi.activities

import androidx.lifecycle.ViewModel
import com.example.ubi.database.Ppk
import com.example.ubi.database.user.User

class MainViewModel: ViewModel() {
    private var _user: User? = null
    private var _ppk: Ppk? = null

    val user get() = _user!!
    val ppk get() = _ppk!!

    fun setUser(user: User) {
        _user = user
    }
    fun setPpk(ppk: Ppk) {
        _ppk = ppk
    }

}