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

    private val urlList: List<List<String>> = listOf(
        listOf("https://www.bankier.pl/fundusze/notowania/PZU55", "PPK inPZU 2035"),
        listOf("https://www.bankier.pl/fundusze/notowania/FOR23", "BNP Paribas PPK 2050"),
        listOf("https://www.bankier.pl/fundusze/notowania/PIO70", "Pekao PPK 2020 Spokojne Jutro"),
        listOf("https://www.bankier.pl/fundusze/notowania/FOR22", "BNP Paribas PPK 2045"),
        listOf("https://www.bankier.pl/fundusze/notowania/INV53", "Investor PPK 2060"),
        listOf("https://www.bankier.pl/fundusze/notowania/BGK22", "Investor PPK 2055"),
        listOf("https://www.bankier.pl/fundusze/notowania/BGK20", "PFR PPK 2045"),
        listOf("https://www.bankier.pl/fundusze/notowania/MIL39", "Millennium Emerytura 2025"),
        listOf("https://www.bankier.pl/fundusze/notowania/ING88", "NN Emerytura 2060"),
        listOf("https://www.bankier.pl/fundusze/notowania/ARK53", "Santander PPK 2055"),
        listOf("https://www.bankier.pl/fundusze/notowania/ARK54", "Santander PPK 2060")


        )
    val ppkList = MutableStateFlow(listOf<Ppk>())
    val isLoading = MutableStateFlow(false)
    val isPpkGot = MutableStateFlow(false)
    val isPpkFailed = MutableStateFlow(false)

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
                isPpkFailed.value = true
                Log.e("JSOUP", e.toString())
            } finally {
                isLoading.value = false
                isPpkGot.value = true
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
            getInformationAboutPpk(url[0]).apply {
                if(this[1].size != 0 && this[0].size != 0)
                    ppks.add(Ppk(url[0].takeLast(5),url[1], this[1], this[0]))
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
            isPpkFailed.value = true
            Log.e("JSOUP", e.toString())
        }
        return listOf<MutableList<String>>(tmstmp, values)
    }

}