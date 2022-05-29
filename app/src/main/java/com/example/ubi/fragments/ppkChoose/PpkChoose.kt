package com.example.ubi.fragments.ppkChoose

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ubi.R
import com.example.ubi.database.Ppk
import com.example.ubi.databinding.FragmentPpkChooseBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.ubi.adapters.PpkRvAdapter as PpkRvAdapter

class PpkChoose : Fragment() {

    private val viewModel by lazy{
        PpkChooseViewModel()
    }

    private val adapter by lazy {
        Log.d("PpkChoose ADAPTER", viewModel.getPpkList().size.toString())
        PpkRvAdapter(arrayListOf())
    }

    private var _binding: FragmentPpkChooseBinding? = null

    private val binding get() =  _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPpkChooseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        collectFlow()
    }

    private fun collectFlow(){
        lifecycleScope.launch{
            viewModel.ppkList.collect {
                adapter.ppkList.clear()
                adapter.ppkList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.swipeRefresh.isRefreshing = it
            }
        }
    }

    private fun setupRecyclerView(){
        binding.ppkRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.ppkRecycleView.adapter = adapter
    }

}