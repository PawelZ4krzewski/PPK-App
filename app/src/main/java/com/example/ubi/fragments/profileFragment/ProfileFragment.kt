package com.example.ubi.fragments.profileFragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import com.example.ubi.R
import com.example.ubi.activities.MainViewModel
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.databinding.FragmentHomeScreenBinding
import com.example.ubi.databinding.FragmentProfileBinding
import com.example.ubi.fragments.homeScreen.HomeScreenViewModel
import java.io.File


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

//    // The path to the root of this app's internal storage
//    private lateinit var privateRootDir: File
//    // The path to the "images" subdirectory
//    private lateinit var imagesDir: File
//    // Array of files in the images subdirectory
//    private lateinit var imageFiles: Array<File>
//    // Array of filenames corresponding to imageFiles
//    private lateinit var imageFilenames: Array<String>

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
            val uriFile = viewModel.exportData(requireContext())

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT,"Ppk export")
            intent.putExtra(Intent.EXTRA_STREAM, uriFile)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent,"Share Via"))
        }

        binding.importDataButton.setOnClickListener {
            pickFile()
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

    private fun pickFile(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "text/plain"
        fileActivityResultLauncher.launch(Intent.createChooser(intent,"Choose a file"))
    }

    private var fileActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),ActivityResultCallback<ActivityResult>{ result ->
            if(result.resultCode == Activity.RESULT_OK){

                val intent = result.data
                val uri = intent?.data
                viewModel.importData(requireContext(), uri!!)
                Toast.makeText(requireContext(),"Open file successful!",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(requireContext(),"Open file ERROR!",Toast.LENGTH_LONG).show()
            }

        }
    )



}