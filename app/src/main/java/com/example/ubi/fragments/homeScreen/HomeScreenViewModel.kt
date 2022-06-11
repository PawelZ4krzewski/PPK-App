package com.example.ubi.fragments.homeScreen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.Ppk
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.User
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.math.RoundingMode
import java.net.URL
import java.text.DecimalFormat
import java.util.*

class HomeScreenViewModel(private val repository: PaymentRepository, application: Application, _user: User) :
    AndroidViewModel(application){

    val user: User = _user

    private val _stateOfFunds = MutableStateFlow("0")
    private val _totalPayment = MutableStateFlow("0")
    private val _ownPayment = MutableStateFlow("0")
    private val _empPayment = MutableStateFlow("0")
    private val _statePayment = MutableStateFlow("0")
    private val _inflationPayment = MutableStateFlow("0")

    private val _userPayments = MutableStateFlow(listOf<Payment>())

    val inflationData = mutableListOf<List<String>>()

    val stateOfFunds get() = _stateOfFunds
    val totalPayment get() = _totalPayment
    val ownPayment get() = _ownPayment
    val empPayment get() = _empPayment
    val statePayment get() = _statePayment
    val inflationPayment get() = _inflationPayment

    val isPaymentGot = MutableStateFlow(false)
    val isInflationGot = MutableStateFlow(true)


    init {
//        getPpk()
        getPayments()
        downloadInflation()
        Log.d("PPKVM", _userPayments.toString())
    }


    fun getPayments(){
        isPaymentGot.value = false
        viewModelScope.launch{
            val payments = repository.getUserPayment(userId = user.userId)
            Log.d("Home Screen", payments.toString())
            if(payments != null){
                _userPayments.value = payments
                isPaymentGot.value = true
            }

        }
    }

    fun setValues(ppk: Ppk){

        stateOfFunds.value = "0"
        ownPayment.value = "0"
        empPayment.value = "0"
        statePayment.value = "0"
        inflationPayment.value = "0"

        _userPayments.value.forEach {
            stateOfFunds.value = (stateOfFunds.value.toFloat() + it.ppkAmount).toString()
            ownPayment.value = (ownPayment.value.toFloat() + it.userPayment).toString()
            empPayment.value = (empPayment.value.toFloat() + it.companyPayment).toString()
            statePayment.value = (statePayment.value.toFloat() + it.countryPayment).toString()
            val total = it.userPayment + it.companyPayment + it.countryPayment
            inflationPayment.value = (inflationPayment.value.toFloat() +  (total * ((getInflation(it.date))/100))).toString()
        }

        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.DOWN

        val todayValue = ppk.values[ppk.values.size - 1].toFloat()
        val fullStateOfFunds = df.format(todayValue * stateOfFunds.value.toFloat())

        stateOfFunds.value = fullStateOfFunds.toString()

        totalPayment.value = (df.format(ownPayment.value.toFloat() + empPayment.value.toFloat() + statePayment.value.toFloat())).toString()

        ownPayment.value = df.format(ownPayment.value.toFloat()).toString()
        empPayment.value = df.format(empPayment.value.toFloat()).toString()
        statePayment.value = df.format(statePayment.value.toFloat()).toString()
        inflationPayment.value = df.format(inflationPayment.value.toFloat()).toString()
    }

    private fun getInflation(timestamp: String): Float {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp.toLong()
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)

        inflationData.forEach {
            if(it[3]==year.toString() && it[4]==month.toString()){
                return it[5].toFloat()
            }
        }
        return 1f
    }
    fun downloadInflation(){

        viewModelScope.launch(Dispatchers.IO) {
            try{
                isInflationGot.value = false
                val url = "https://stat.gov.pl/download/gfx/portalinformacyjny/pl/defaultstronaopisowa/4741/1/1/miesieczne_wskazniki_cen_towarow_i_uslug_konsumpcyjnych_od_1982_roku_13-05-2022.csv"
                val strona = URL(url).readText()

                val tsvReader = csvReader {
                    delimiter = ';'
                    skipEmptyLine = true
                }

                val rows: List<List<String>> = tsvReader.readAll(strona)

                rows.forEach {
                    if (it[2].take(17) == "Analogiczny miesi") {
                        inflationData.add(it)
                    }
                }
            }catch (e:Exception){
                isInflationGot.value = false
                Log.e("Home Screen", e.toString())
            }
            finally {
                isInflationGot.value = true
            }
        }
    }

}