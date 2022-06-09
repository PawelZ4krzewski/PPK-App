package com.example.ubi.fragments.countryPayment

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
import com.example.ubi.activities.MainViewModel
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.databinding.FragmentCountryPaymentBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class CountryPaymentFragment : Fragment() {

    private val viewModel by lazy{
        val application = requireNotNull(this.activity).application
        val dao = PPKDatabase.getDatabase(application).PaymentDao()
        val repository = PaymentRepository(dao)
        CountryPaymentViewModel(repository,application)
    }

    private val mainViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentCountryPaymentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCountryPaymentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countryPaymentTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setCountryPayment(text.toString())
        }

        binding.unitValuetTextInputEditText.doOnTextChanged { text, _, _, _ ->
            viewModel.setUnitValue(text.toString())
        }

        binding.addPaymentButton.setOnClickListener {
            viewModel.addPayment(mainViewModel.user.userId)
        }

        binding.unitValuetTextInputEditText.text = mainViewModel.ppk.values[mainViewModel.ppk.values.size-1]

        setupDatePicker()
        collectFlow()

    }

    private fun collectFlow() {

        lifecycleScope.launch {
            viewModel.addPaymentToast.collect {
                if (it) {
                    Toast.makeText(requireContext(), "Payment is added!", Toast.LENGTH_LONG).show()
                    viewModel.addPaymentToast.value = false
                    binding.countryPaymentTextInputEditText.setText(viewModel.countryPayment.value)
                    binding.dateInputEditText.setText(viewModel.date.value)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isAddPaymentEnable.collect {
                binding.addPaymentButton.isEnabled = it
            }
        }
    }

    private fun changeUnitValue(){
        val date = viewModel.date.value
        var minDate = Float.POSITIVE_INFINITY
        var index = 0
        mainViewModel.ppk.dates.forEachIndexed(){i,ppkDate ->
            if(minDate > Math.abs(ppkDate.toFloat() - date.toFloat()) && ppkDate.toFloat() <= date.toFloat()){
                minDate = Math.abs(ppkDate.toFloat() - date.toFloat())
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
            DatePickerDialog(requireContext(), datePicker, calendar.get(Calendar.YEAR), calendar.get(
                Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun updateLabel(calendar: Calendar) {
        val myFormat = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.GERMANY)
        binding.dateInputEditText.setText(sdf.format(calendar.time))
    }

}