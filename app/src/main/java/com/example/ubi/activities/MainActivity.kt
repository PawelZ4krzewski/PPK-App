package com.example.ubi.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.example.ubi.R
import com.example.ubi.fragments.homeScreen.HomeScreenFragmentArgs

class MainActivity : AppCompatActivity() {

    private val args by navArgs<HomeScreenFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainModel: MainViewModel by viewModels()
        mainModel.setUser(args.currentUser)
    }
}