package com.example.ubi.fragments.ppkChoose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ubi.R
import com.example.ubi.database.Ppk
import com.example.ubi.databinding.FragmentPpkChooseBinding
import com.example.ubi.adapters.PpkRvAdapter as PpkRvAdapter

class PpkChoose : Fragment() {

    private val ppkList: ArrayList<Ppk> = arrayListOf(
        Ppk("Nazwa 1", 1.54F),
        Ppk("Nazwa 2", 2.54F),
        Ppk("Nazwa 3", 3.54F),
        Ppk("Nazwa 4", 4.54F),
        Ppk("Nazwa 5", 5.54F),
    )

    private val viewModel by lazy{
        PpkChooseViewModel()
    }

    private val adapter by lazy {
        PpkRvAdapter(ppkList)
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
    }


    private fun setupRecyclerView(){
        binding.ppkRecycleView.layoutManager = LinearLayoutManager(requireContext())
        binding.ppkRecycleView.adapter = adapter
    }

}