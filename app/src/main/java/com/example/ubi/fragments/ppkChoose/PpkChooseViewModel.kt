package com.example.ubi.fragments.ppkChoose

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.Ppk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class PpkChooseViewModel : ViewModel() {

    private val urlList: List<String> = listOf("https://www.bankier.pl/fundusze/notowania/PZU55")
    val ppkList = MutableStateFlow(listOf<Ppk>())
    val isLoading = MutableStateFlow(false)

    init {
        getPpk()
    }

     fun getPpk() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                isLoading.value = true
                val response = makePpk()
                delay(500)
                ppkList.value = response

                Log.d("PPKVM", ppkList.value.size.toString())
            } catch (e: Exception) {
                Log.e("JSOUP", e.toString())
            } finally {
                isLoading.value = false
            }
        }
    }

    fun getPpkList(): List<Ppk> {
        Log.d("PPKVM", ppkList.value.size.toString())

        return ppkList.value
    }

    fun makePpk(): List<Ppk> {

        val ppks: MutableList<Ppk> = mutableListOf()

        urlList.forEach { url ->
            getInformationAboutPpk(url).apply {
                ppks.add(Ppk("PZU55", this[1], this[0]))
            }
        }

        return ppks.toList()
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


    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}