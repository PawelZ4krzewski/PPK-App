package com.example.ubi.fragments.ppkChoose

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.Ppk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*

class PpkChooseViewModel: ViewModel() {

    private val urlList: List<String> = listOf("https://www.bankier.pl/fundusze/notowania/PZU55")
    private val ppkList: MutableList<Ppk> = mutableListOf()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            try {
                getInformationAboutPpk(urlList[0]).apply {
                    ppkList.add(Ppk("PZU55",this[1], this[0]))
                }
            } catch (e: Exception) {
                Log.e("JSOUP", e.toString())
            }

        }
    }


    val inf = getInformationAboutPpk(urlList[0])




    private fun getInformationAboutPpk(url: String): List<MutableList<String>> {

        val tmstmp: MutableList<String> = mutableListOf()
        val values: MutableList<String> = mutableListOf()

        try {

            val document = Jsoup.connect(url).get()
            var daneNazwa: String? = null

            Log.d("Document",document.outerHtml())

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
                        Log.d("DODAJE", x.toString() +" "+ value.toString())
                        tmstmp.add(x)
                        values.add(value)
                    }
                }
            }
        }
        catch(e : Exception){
            Log.e("JSOUP", e.toString())
        }
        return listOf<MutableList<String>>(tmstmp,values)
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