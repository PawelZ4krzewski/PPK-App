package com.example.ubi.fragments.homeScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ubi.R
import com.example.ubi.activities.LoginViewModel
import com.example.ubi.activities.MainViewModel
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.UserRepository
import com.example.ubi.databinding.FragmentHomeScreenBinding
import com.example.ubi.fragments.ppkChoose.PpkChooseViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeScreenFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private val viewModel by lazy{

        val application = requireNotNull(this.activity).application

        val dao = PPKDatabase.getDatabase(application).PaymentDao()

        val repository = PaymentRepository(dao)


        HomeScreenViewModel(repository,application, mainViewModel.user)
    }

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding
                    get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Log.d("Home Screen", viewModel.user.toString())

        binding.addPaymentButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreenFragment_to_employeePaymentFragment2)
        }

        setValues()
        collectFlow()
    }

    private fun setValues(){
        binding.ppkName.text = mainViewModel.user.ppkName
        binding.TotalFunds.text = viewModel.stateOfFunds.value + " zł"
        binding.paymentValue.text = viewModel.totalPayment.value + " zł"
        binding.ownPaymentValue.text = viewModel.ownPayment.value + " zł"
        binding.empPaymentValue.text = viewModel.empPayment.value + " zł"
        binding.countryPaymentValue.text = viewModel.statePayment.value + " zł"
        binding.inflationValue.text = viewModel.inflationPayment.value + " zł"
    }

    private fun collectFlow(){

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.swipeRefresh.isRefreshing = it
                binding.addPaymentButton.isClickable = !it
            }
        }

        lifecycleScope.launch{
            viewModel.isPpkGot.collect{
                if(it){
                    Log.d("HomeScreen",viewModel.ppk.toString())
                    mainViewModel.setPpk(viewModel.ppk)
                }
            }
        }
    }
}