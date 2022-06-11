package com.example.ubi.activities

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.Ppk
import com.example.ubi.database.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class MainViewModel: ViewModel() {
    private var _user: User? = null
    private var _ppk: Ppk? = null

    val user get() = _user!!
    val ppk get() = _ppk!!

    val isPpkGot = MutableStateFlow(false)


    fun setUser(user: User) {
        _user = user
    }

    fun setPpk(ppk: Ppk) {
        _ppk = ppk
        isPpkGot.value = true
    }

}