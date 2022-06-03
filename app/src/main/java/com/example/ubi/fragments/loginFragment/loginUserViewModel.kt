package com.example.ubi.fragments.loginFragment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class loginUserViewModel: ViewModel() {
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

}