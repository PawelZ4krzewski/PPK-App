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


    val isLoading = MutableStateFlow(false)
    val isPpkGot = MutableStateFlow(false)

    init {
        Log.d("MainActivity", "Tworze MainViewModel")
        getPpk()
    }

    fun setUser(user: User) {
        _user = user
    }

    fun setPpk(ppk: Ppk) {
        _ppk = ppk
    }

    private fun getPpk() {
        Log.d("Main Activity", "Zaczynam pobierać PPK")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.value = true
                val response = makePpk()
                Log.d("Main Activity", "Otrzymalem response")
                _ppk = response

                Log.d("Main Activity", ppk.values.toString())
                isLoading.value = false
                isPpkGot.value = true
            } catch (e: Exception) {
                Log.e("JSOUP", e.toString())
            } finally {
                Log.d("Main Activity","PPK Można dodawać")
                isLoading.value = false
                isPpkGot.value = true
            }
        }
    }

    private fun makePpk(): Ppk {
        Log.d("Main Activity", "Make PPK")

        val url = "https://www.bankier.pl/fundusze/notowania/" + user.ppkId
        getInformationAboutPpk(url).apply {
            Log.d("Main Activity", "Jestem w apply")
            return Ppk(user.ppkId, user.ppkName, this[1], this[0])
        }
    }

    private fun getInformationAboutPpk(url: String): List<MutableList<String>> {

        Log.d("Main Activity", "Get Information PPK")

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
        Log.d("Main Activity", "Returnuje liste")
        return listOf<MutableList<String>>(tmstmp, values)
    }

}