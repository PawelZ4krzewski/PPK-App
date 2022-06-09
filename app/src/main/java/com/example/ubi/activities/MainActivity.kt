package com.example.ubi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.ubi.R
import com.example.ubi.fragments.countryPayment.CountryPaymentFragment
import com.example.ubi.fragments.employeePayment.EmployeePaymentFragment
import com.example.ubi.fragments.homeScreen.HomeScreenFragment
import com.example.ubi.fragments.homeScreen.HomeScreenFragmentArgs
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val args by navArgs<HomeScreenFragmentArgs>()

    private val homeFragment = HomeScreenFragment()
    private val empPaymentFragment = EmployeePaymentFragment()
    private val countryPaymentFragment = CountryPaymentFragment()
//    private val userFragment = userFragment()

    override fun onCreate(savedInstanceState: Bundle?) {

        supportActionBar?.hide()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragment(homeFragment)

        val mainModel: MainViewModel by viewModels()
        mainModel.setUser(args.currentUser)

        val bottom_navigation = findViewById<BottomNavigationView>(R.id.mainBottomNavigation)
        bottom_navigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(homeFragment)
                R.id.addPayment -> replaceFragment(empPaymentFragment)
//                R.id.profile -> replaceFragment(empPaymentFragment)
            }
            true
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