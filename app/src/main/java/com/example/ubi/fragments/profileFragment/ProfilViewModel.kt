package com.example.ubi.fragments.profileFragment

import android.app.Application
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.InputStreamReader
import java.nio.file.Paths
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ProfilViewModel(private val repository: PaymentRepository, application: Application, _user: User) :
    AndroidViewModel(application){

    private val user = _user
    private val _userPayments = MutableStateFlow(listOf<Payment>())

    val isPaymentGot = MutableStateFlow(false)

    init {
        getPayments()
    }


    fun getPayments(){

        isPaymentGot.value = false
        viewModelScope.launch{
            val payments = repository.getUserPayment(userId = user.userId)
            Log.d("Profil view", payments.toString())
            if(payments != null){
                _userPayments.value = payments
                isPaymentGot.value = true
            }
        }
    }

    fun exportData(context: Context): Uri{

        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())

        val myFile = File(context.filesDir, "export_" + user.userName +"_" + currentDate+ ".txt")
        val isFile = myFile.createNewFile()
        if (isFile) {
            Log.d("Profil view", "Utworzono nowy plik")

        } else {
            Log.d("Profil view", "Plik juz istnieje")
        }

        if(isPaymentGot.value){
            myFile.writeText(user.userName+";"+user.userPassword+"\n")
            _userPayments.value.forEach{
                myFile.appendText(it.paymentId.toString()+";"+it.userId.toString()+";"+it.userPayment.toString()+";"+it.companyPayment.toString()+";"+it.countryPayment.toString()+";"+it.ppkAmount.toString()+";"+it.date+"\n")
            }
        }
        else{
            viewModelScope.launch {
                isPaymentGot.collect {
                    if(it){
                        myFile.writeText(user.userName+";"+user.userPassword+"\n")
                        _userPayments.value.forEach{
                            myFile.appendText(it.paymentId.toString()+";"+it.userId.toString()+";"+it.userPayment.toString()+";"+it.companyPayment.toString()+";"+it.countryPayment.toString()+";"+it.ppkAmount.toString()+";"+it.date+"\n")
                        }
                    }
                }
            }
        }


        return FileProvider.getUriForFile(context,"com.example.ubi.fileprovider",myFile)
    }

    fun importData(context:Context, uri: Uri): Boolean{

        val stream = context.contentResolver.openInputStream(uri)
        val reader = BufferedReader(InputStreamReader(stream))

        var line = reader.readLine()
        var tekst = line.split(";")
        if(tekst[0] == user.userName && tekst[1] == user.userPassword){

            line = reader.readLine()

            try {

                while (line != null) {

                    tekst = line.split(";")

                    val payment = Payment(0, tekst[1].toInt(), tekst[2].toFloat(), tekst[3].toFloat(), tekst[4].toFloat(), tekst[5].toFloat(), tekst[6])

                    viewModelScope.launch(Dispatchers.IO) {
                        try {
                            repository.addPayment(payment)
                            Log.d("Profil view", "DODANO PAYMENT")
                        }
                        catch(e: Exception){
                            Log.e("Profil view","During add to database" + e.toString())
                        }
                    }

                    line = reader.readLine()
                }
            }catch (e: Exception){
                Log.e("Profil view", "Outside" + e.toString())
                return false
            }

            return true
        }
        else{
            return false
        }


//        Log.d("Profil", toString())
    }
}