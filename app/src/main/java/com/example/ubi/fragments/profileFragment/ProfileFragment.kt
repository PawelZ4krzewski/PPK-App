package com.example.ubi.fragments.profileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.ubi.R
import com.example.ubi.activities.MainViewModel
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.databinding.FragmentHomeScreenBinding
import com.example.ubi.databinding.FragmentProfileBinding
import com.example.ubi.fragments.homeScreen.HomeScreenViewModel


class ProfileFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentProfileBinding? = null
    private val binding
        get() = _binding!!


    private val viewModel by lazy{

        val application = requireNotNull(this.activity).application

        val dao = PPKDatabase.getDatabase(application).PaymentDao()

        val repository = PaymentRepository(dao)

        ProfilViewModel(repository,application, mainViewModel.user)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.exportDataButton.setOnClickListener {
            viewModel.exportData(requireContext())
        }

        setValues()
    }

    private fun setValues(){
        binding.usernameTextView.text = mainViewModel.user.userName
        binding.companyNameTextView.text = mainViewModel.user.companyName
        binding.extUserPerTextView.text = mainViewModel.user.userPercentage.toString()
        binding.extCompPerTextView.text = mainViewModel.user.companyPercentage.toString()
        binding.ppkNameTextView.text = mainViewModel.user.ppkName
    }

}