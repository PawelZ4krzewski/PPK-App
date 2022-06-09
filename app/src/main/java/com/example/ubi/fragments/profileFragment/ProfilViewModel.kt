package com.example.ubi.fragments.profileFragment

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ubi.database.payment.Payment
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVPrinter
import java.io.BufferedWriter
import java.io.File
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
            Log.d("Home Screen", payments.toString())
            if(payments != null){
                _userPayments.value = payments
                isPaymentGot.value = true
            }
        }
    }

    fun exportData(context: Context): Uri{

        val sdf = SimpleDateFormat("dd-M-yyyy")
        val currentDate = sdf.format(Date())

        val fileName = "src/resources/export_"+ user.userName +"_" + currentDate+ ".txt"

        val myFile = File(context.filesDir, "export_" + user.userName +"_" + currentDate+ ".txt")
        val isFile = myFile.createNewFile()
        if (isFile) {
            Log.d("SettingsFragment", "Utworzono nowy plik")

        } else {
            Log.d("SettingsFragment", "Plik juz istnieje")
        }

        myFile.writeText(user.userName+";"+user.userPassword+"\n")
        _userPayments.value.forEach{
            myFile.appendText(it.paymentId.toString()+";"+it.userId.toString()+";"+it.userPayment.toString()+";"+it.companyPayment.toString()+";"+it.countryPayment.toString()+";"+it.ppkAmount.toString()+";"+it.date+"\n")
        }

        return FileProvider.getUriForFile(context,"com.example.ubi.fileprovider",myFile)
    }

    fun importData(context:Context, uri: Uri){

        val mimeType = uri.let { returnUri ->
            context.contentResolver.getType(returnUri)
        }
        Log.d("Profil", mimeType.toString())
    }
}