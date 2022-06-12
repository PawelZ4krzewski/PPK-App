package com.example.ubi.fragments.paymentHistory

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ubi.R
import com.example.ubi.activities.MainViewModel
import com.example.ubi.adapters.PaymentRvAdapter
import com.example.ubi.adapters.PpkRvAdapter
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.UserPayment
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.databinding.FragmentPaymentHistoryBinding
import com.example.ubi.fragments.homeScreen.HomeScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class PaymentHistoryFragment : Fragment() {

    private val adapter by lazy {
        PaymentRvAdapter(arrayListOf())
    }

    private val mainViewModel: MainViewModel by activityViewModels()


    private val viewModel by lazy{
        val application = requireNotNull(this.activity).application
        val dao = PPKDatabase.getDatabase(application).PaymentDao()
        val repository = PaymentRepository(dao)
        PaymentHistoryViewModel(repository,application)
    }

    private var _binding: FragmentPaymentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentHistoryBinding.inflate(inflater,container,false)
        viewModel.getPayments(mainViewModel.user.userId)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlow()
        setupRecyclerView()
    }

    private fun collectFlow(){
        lifecycleScope.launch{
            viewModel.userPayments.collect {
                    Log.d("PaymentHistory", "Dodaje dane do RV $it")
                    adapter.userPaymentList.clear()
                    adapter.userPaymentList.addAll(it)
                    adapter.notifyDataSetChanged()
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.swipeRefresh.isRefreshing = it
            }
        }
        lifecycleScope.launch {
            viewModel.isPaymentGot.collect {
                if(it){
                    Log.d("PaymentHistory", "Dodaje dane do RV $it")
                    adapter.userPaymentList.clear()
                    adapter.userPaymentList.addAll(viewModel.userPayments.value)
                    adapter.notifyDataSetChanged()
                }
            }
        }

    }

    private fun setupRecyclerView(){
        binding.historyRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecycleView.adapter = adapter
    }


}