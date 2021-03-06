package com.example.ubi.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.Window
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.ubi.NetworkConnection
import com.example.ubi.R
import com.example.ubi.fragments.countryPayment.CountryPaymentFragment
import com.example.ubi.fragments.employeePayment.EmployeePaymentFragment
import com.example.ubi.fragments.homeScreen.HomeScreenFragment
import com.example.ubi.fragments.homeScreen.HomeScreenFragmentArgs
import com.example.ubi.fragments.profileFragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File


class MainActivity : AppCompatActivity() {


    private val args by navArgs<HomeScreenFragmentArgs>()

    private val mainModel: MainViewModel by viewModels()

    private val homeFragment = HomeScreenFragment()
    private val empPaymentFragment = EmployeePaymentFragment()
    private val countryPaymentFragment = CountryPaymentFragment()
    private val userFragment = ProfileFragment()


    override fun onCreate(savedInstanceState: Bundle?) {


        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(homeFragment)

        mainModel.setUser(args.currentUser)

        navigate()

        val networkConnection = NetworkConnection(applicationContext)

        networkConnection.observe(this, Observer {  isConnected ->
            if(isConnected){
                mainModel.isInternet.value = true
                findViewById<View>(R.id.noInternetLayout).visibility = GONE
                findViewById<ConstraintLayout>(R.id.noInternetWhiteBox).visibility = GONE
            }
            else
            {
                mainModel.isInternet.value = false
                findViewById<View>(R.id.noInternetLayout).visibility = VISIBLE
                findViewById<ConstraintLayout>(R.id.noInternetWhiteBox).visibility = VISIBLE
            }
        })


    }

    private fun navigate()
    {
        lifecycleScope.launch {
            mainModel.isPpkGot.collect {
                if (it) {
                    val bottom_navigation =
                        findViewById<BottomNavigationView>(R.id.mainBottomNavigation)
                    bottom_navigation.setOnItemSelectedListener {
                        when (it.itemId) {
                            R.id.home -> replaceFragment(homeFragment)
                            R.id.addPayment -> replaceFragment(empPaymentFragment)
                            R.id.addCountryPayment -> replaceFragment(countryPaymentFragment)
                            R.id.profile -> replaceFragment(userFragment)
                        }
                        true
                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.commit()
        }
    }

}
