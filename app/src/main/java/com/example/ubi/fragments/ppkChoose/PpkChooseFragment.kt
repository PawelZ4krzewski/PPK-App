package com.example.ubi.fragments.ppkChoose

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ubi.activities.LoginViewModel
import com.example.ubi.database.user.User
import com.example.ubi.database.user.UserVIewModel
import com.example.ubi.databinding.FragmentPpkChooseBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import java.math.BigInteger
import java.security.MessageDigest
import java.util.Calendar.SHORT
import com.example.ubi.adapters.PpkRvAdapter as PpkRvAdapter

class PpkChooseFragment : Fragment() {

    private val viewModel by lazy{
        PpkChooseViewModel()
    }

    private val loginViewModel: LoginViewModel by activityViewModels()

    private lateinit var mUserViewModel: UserVIewModel

    private val adapter by lazy {
//        Log.d("PpkChoose ADAPTER", viewModel.getPpkList().size.toString())
        PpkRvAdapter(arrayListOf()){ ppk ->
            loginViewModel.setPpkId(ppk.id)
            loginViewModel.setPpkName(ppk.name)
            loginViewModel.printRegistrationInfo()
            insertDataToDatabase()
        }
    }

    private var _binding: FragmentPpkChooseBinding? = null

    private val binding get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPpkChooseBinding.inflate(inflater, container, false)

        mUserViewModel = ViewModelProvider(this).get(UserVIewModel::class.java)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getPpk()
        }

        setupRecyclerView()
        collectFlow()
    }

    private fun collectFlow(){
        lifecycleScope.launch{
            viewModel.ppkList.collect {
                if(it.isNotEmpty()){
                    adapter.ppkList.clear()
                    adapter.ppkList.addAll(it)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.swipeRefresh.isRefreshing = it
            }
        }

        lifecycleScope.launch {
            viewModel.isPpkFailed.collect{  it ->
                if(it){
                    Toast.makeText(requireContext(),"Something went wrong! Try later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView(){
        binding.ppkRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.ppkRecycleView.adapter = adapter
    }

    private fun insertDataToDatabase(){
        val userName = loginViewModel.username!!
        val password = loginViewModel.password!!
        val additionalPercentage  = loginViewModel.additionalPercentage!!
        val companyName  = loginViewModel.companyName!!
        val additionalCompanyPercentage = loginViewModel.additionalCompanyPercentage!!
        val ppkId = loginViewModel.ppkId!!
        val ppkName = loginViewModel.ppkName!!

        val user = User(0,userName, md5(password), additionalPercentage.toFloat(), companyName, additionalCompanyPercentage.toFloat(), ppkId, ppkName)
        //Add Data to Database

        try {
            mUserViewModel.addUser(user)
            Toast.makeText(requireContext(),"Successfully added!", Toast.LENGTH_LONG).show()
            val action = PpkChooseFragmentDirections.actionPpkChooseToLoginFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }catch (e: Exception){
            Toast.makeText(requireContext(),"Registration error $e!", Toast.LENGTH_LONG).show()
        }

    }

    fun md5(input:String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}