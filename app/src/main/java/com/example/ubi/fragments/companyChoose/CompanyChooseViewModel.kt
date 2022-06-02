package com.example.ubi.fragments.companyChoose

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

class CompanyChooseViewModel: ViewModel() {

    private val _additionalPercentage = MutableStateFlow("")
    private val _companyName = MutableStateFlow("")
    private val _additionalCompanyPercentage = MutableStateFlow("")

    val isAdditionalEnable = combine(_additionalPercentage, _companyName, _additionalCompanyPercentage) { emplExtPer, companyName, addCompPer ->
        return@combine emplExtPer.isNotBlank() && companyName.isNotBlank() && addCompPer.isNotBlank()
    }


    fun setAdditionalPercentage(value: String) {
        _additionalPercentage.value = value
    }

    fun setCompanyName(name: String) {
        _companyName.value = name
    }

    fun setAdditionalCompanyPercentage(value: String) {
        _additionalCompanyPercentage.value = value
    }

    fun getAdditionalPercentage(): String {
        return _additionalPercentage.value
    }

    fun getCompanyName(): String {
        return _companyName.value
    }

    fun getAdditionalCompanyPercentage():String {
        return _additionalCompanyPercentage.value
    }
}