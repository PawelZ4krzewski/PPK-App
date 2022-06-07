package com.example.ubi.fragments.homeScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.Ppk
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.User
import com.example.ubi.database.user.UserRepository
import com.example.ubi.fragments.loginFragment.LoginFragmentDirections
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jsoup.Jsoup

class HomeScreenViewModel(private val repository: PaymentRepository, application: Application, _user: User) :
    AndroidViewModel(application){

    private val user: User = _user
    private var _ppk: Ppk? = null

    private val _stateOfFunds = MutableStateFlow("0")
    private val _totalPayment = MutableStateFlow("0")
    private val _ownPayment = MutableStateFlow("0")
    private val _empPayment = MutableStateFlow("0")
    private val _statePayment = MutableStateFlow("0")
    private val _inflationPayment = MutableStateFlow("0")

    private val _userPayments = MutableStateFlow(listOf<Payment>())


    val ppk get() = _ppk!!
    val stateOfFunds get() = _stateOfFunds
    val totalPayment get() = _totalPayment
    val ownPayment get() = _ownPayment
    val empPayment get() = _empPayment
    val statePayment get() = _statePayment
    val inflationPayment get() = _inflationPayment


    val isLoading = MutableStateFlow(false)
    val isPpkGot = MutableStateFlow(false)

    init {
        getPpk()
        getPayments()


        Log.d("PPKVM", _userPayments.toString())
    }

    fun getPpk() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.value = true
                val response = makePpk()
                delay(500)
                _ppk = response

                Log.d("PPKVM", ppk.toString())
            } catch (e: Exception) {
                Log.e("JSOUP", e.toString())
            } finally {
                Log.d("HomeScreen","PPK Można dodawać")
                isLoading.value = false
                isPpkGot.value = true
            }
        }
    }

    fun makePpk(): Ppk {

            val url = "https://www.bankier.pl/fundusze/notowania/" + user.ppkId
            getInformationAboutPpk(url).apply {
                return Ppk(user.ppkId, user.ppkName, this[1], this[0])
            }
    }

    private fun getInformationAboutPpk(url: String): List<MutableList<String>> {

        val tmstmp: MutableList<String> = mutableListOf()
        val values: MutableList<String> = mutableListOf()

        try {

            val document = Jsoup.connect(url).get()
            var daneNazwa: String? = null

            for (row in document.getElementsByTag("script")) {
                if ("dane_nazwa = " in row.toString()) {
                    for (line in row.toString().lines()) {
                        if ("dane_nazwa " in line) {
                            daneNazwa = line
                        }
                    }
                }
            }

            if (daneNazwa != null) {
                daneNazwa = daneNazwa.substring(18, daneNazwa.length - 2)

                var value: String?
                var x: String?

                for (obj in daneNazwa.split("}")) {
                    value = obj.substringAfter("\"y\":").substringBefore(",\"turnover\"")
                    x = obj.substringAfter("\"x\":")
                    if (!value.isNullOrBlank() and !x.isNullOrBlank()) {
//                        Log.d("DODAJE", x.toString() +" "+ value.toString())
                        tmstmp.add(x)
                        values.add(value)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("JSOUP", e.toString())
        }
        return listOf<MutableList<String>>(tmstmp, values)
    }

    fun getPayments(){
        viewModelScope.launch{
            val payments = repository.getUserPayment(userId = user.userId)
            Log.d("Home Screen", payments.toString())
            if(payments != null){
                _userPayments.value = payments
            }
        }
    }

}