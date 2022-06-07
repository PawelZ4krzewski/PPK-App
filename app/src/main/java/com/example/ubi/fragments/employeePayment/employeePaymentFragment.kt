package com.example.ubi.fragments.employeePayment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.ubi.R
import com.example.ubi.activities.MainViewModel
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.databinding.FragmentEmployeePaymentBinding
import com.example.ubi.fragments.homeScreen.HomeScreenViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Float.POSITIVE_INFINITY
import java.lang.Math.abs
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*


class employeePaymentFragment : Fragment() {

    private val viewModel by lazy{
        val application = requireNotNull(this.activity).application
        val dao = PPKDatabase.getDatabase(application).PaymentDao()
        val repository = PaymentRepository(dao)
        employeePaymentViewModel(repository,application)
    }

    private val mainViewModel: MainViewModel by activityViewModels()


    private var _binding: FragmentEmployeePaymentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmployeePaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.extEmpPerTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setOwnPayment(text.toString())
        }

        binding.extEmpPerTextInputEditText.setOnFocusChangeListener { view, b ->
            val text = binding.extEmpPerTextInputEditText.text.toString()
            if(text.isNotBlank()) {
                val companyValue = String.format(
                    "%.2f",
                    ((100 * text.toFloat() / (2 + mainViewModel.user.userPercentage)) * ((mainViewModel.user.companyPercentage + 1.5f)/ 100))
                )
                viewModel.setEmpPayment(companyValue)
                binding.extCompanyPaymentTextInputEditText.setText(companyValue)
            }
        }

        binding.extCompanyPaymentTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setEmpPayment(text.toString())
        }

        binding.extCompanyPaymentTextInputEditText.setOnFocusChangeListener { view, b ->
            val text = binding.extCompanyPaymentTextInputEditText.text.toString()
            if(text.isNotBlank()) {
                val ownValue = String.format(
                    "%.2f",
                    ((100 * text.toFloat() / (1.5f + mainViewModel.user.companyPercentage)) * ((2 + mainViewModel.user.userPercentage) / 100))
                )
                viewModel.setOwnPayment(ownValue)
                binding.extEmpPerTextInputEditText.setText(ownValue)
            }
        }

        binding.addPaymentButton.setOnClickListener {
            viewModel.addPayment(mainViewModel.user.userId, mainViewModel.ppk.values[mainViewModel.ppk.values.size-1])
        }

//        binding.dateInputEditText.doOnTextChanged{text,_,_,_ ->

//            if(viewModel.date.value == ""){
//                binding.unitValuetTextInputEditText.text = mainViewModel.ppk.values[mainViewModel.ppk.values.size-1]
//            }
//            else{
//                val date = viewModel.date.value
//                var minDate = POSITIVE_INFINITY
//                var index = 0
//                mainViewModel.ppk.dates.forEachIndexed(){i,ppkDate ->
//                    if(minDate > abs(ppkDate.toFloat() - date.toFloat())  && ppkDate.toFloat() <= date.toFloat()){
//                        minDate = abs(ppkDate.toFloat() - date.toFloat())
//                        index = i
//                    }
//                }
//                binding.unitValuetTextInputEditText.text = mainViewModel.ppk.values[index]
//            }
//        }

        binding.unitValuetTextInputEditText.text = mainViewModel.ppk.values[mainViewModel.ppk.values.size-1]

        setupDatePicker()
        collectFlow()
    }

    private fun collectFlow() {

        lifecycleScope.launch {
            viewModel.addPaymentToast.collect {
                if(it){
                    Toast.makeText(requireContext(),"Payment is added!", Toast.LENGTH_LONG).show()
                    viewModel.addPaymentToast.value = false
                    binding.extEmpPerTextInputEditText.setText(viewModel.ownPayment.value)
                    binding.extCompanyPaymentTextInputEditText.setText(viewModel.ownPayment.value)
                    binding.dateInputEditText.setText(viewModel.date.value)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isAddPaymentEnable.collect {
                binding.addPaymentButton.isEnabled = it
            }
        }

//        lifecycleScope.launch {
//            viewModel.ownPayment.collect {
//                if(it.isNotBlank()){
//                    val companyValue = String.format(
//                        "%.2f",
//                        ((100 * it.toFloat() / mainViewModel.user.userPercentage) * (mainViewModel.user.companyPercentage / 100))
//                    )
//                    viewModel.setEmpPayment(companyValue)
//                    binding.extCompanyPaymentTextInputEditText.setText(companyValue)
//                }
//            }
//        }

//        lifecycleScope.launch {
//            viewModel.empPayment.collect {
//                if(it.isNotBlank()) {
//                    val ownValue = String.format(
//                        "%.2f",
//                        ((100 * it.toFloat() / mainViewModel.user.companyPercentage) * (mainViewModel.user.userPercentage / 100))
//                    )
//                    viewModel.setOwnPayment(ownValue)
//                    binding.extEmpPerTextInputEditText.setText(ownValue)
//                }
//            }
//        }
    }

    private fun changeUnitValue(){
        val date = viewModel.date.value
        var minDate = POSITIVE_INFINITY
        var index = 0
        mainViewModel.ppk.dates.forEachIndexed(){i,ppkDate ->
            if(minDate > abs(ppkDate.toFloat() - date.toFloat())  && ppkDate.toFloat() <= date.toFloat()){
                minDate = abs(ppkDate.toFloat() - date.toFloat())
                index = i
            }
        }
        binding.unitValuetTextInputEditText.text = mainViewModel.ppk.values[index]
    }
    private fun setupDatePicker(){

        val tvdatePicker = binding.dateInputEditText

        val calendar = Calendar.getInstance()


        val datePicker = DatePickerDialog.OnDateSetListener{ view, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            calendar.set(Calendar.HOUR, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            updateLabel(calendar)
            Log.d("Date",calendar.timeInMillis.toString())
            viewModel.setDate(calendar.timeInMillis.toString())
            changeUnitValue()
        }


        tvdatePicker.setOnClickListener {
            DatePickerDialog(requireContext(), datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateLabel(calendar: Calendar) {
        val myFormat = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
        binding.dateInputEditText.setText(sdf.format(calendar.time))
    }


}