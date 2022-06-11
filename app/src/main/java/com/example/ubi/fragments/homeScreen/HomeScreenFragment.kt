package com.example.ubi.fragments.homeScreen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ubi.NetworkConnection
import com.example.ubi.R
import com.example.ubi.activities.LoginViewModel
import com.example.ubi.activities.MainViewModel
import com.example.ubi.adapters.PpkRvAdapter
import com.example.ubi.database.PPKDatabase
import com.example.ubi.database.Ppk
import com.example.ubi.database.payment.PaymentRepository
import com.example.ubi.database.user.UserRepository
import com.example.ubi.databinding.FragmentHomeScreenBinding
import com.example.ubi.fragments.ppkChoose.PpkChooseViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
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

        collectFlow()
        setValues()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getPayments()
        Log.d("HomeScreen","On Resume")
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
            mainViewModel.isInternet.collect{  isConnected ->
                if(isConnected){
                    if(!viewModel.isDataDownloaded.value){
                        viewModel.startViewModel()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                binding.swipeRefresh.isRefreshing = it
                binding.swipeRefresh.isEnabled = it
            }
        }

        lifecycleScope.launch{
            viewModel.isPpkGot.collect{
                if(it){
                    Log.d("HomeScreen","We have PPK")
                    mainViewModel.setPpk(viewModel.ppk)
                    //Chart
                    initChart(mainViewModel.ppk)
                    setDataToLineChart(mainViewModel.ppk)

                    if(viewModel.isPaymentGot.value && viewModel.isInflationGot.value){
                        viewModel.setValues()
                        setValues()
                        Log.d("HomeScreen","Values are set in is PPKGot")
                    }
                    else
                    {
                        Log.d("HomeScreen","isPaymentGot lub isInflationGot nie jest got a Ppk jest")
                    }
                }
            }
        }

        lifecycleScope.launch{
            viewModel.isPaymentGot.collect{
                if(it){
                    if(mainViewModel.isPpkGot.value && viewModel.isInflationGot.value){
                        viewModel.setValues()
                        setValues()
                    }
                    Log.d("HomeScreen","isPpkGot lub isInflationGot nie jest got a Payment jest")
                }
            }
        }

        lifecycleScope.launch{
            viewModel.isInflationGot.collect{
                if(it){
                    if(mainViewModel.isPpkGot.value && viewModel.isPaymentGot.value){
                        viewModel.setValues()
                        setValues()
                    }
                    Log.d("HomeScreen","isPpkGot lub isPaymentGot nie jest got a Inflation jest")
                }
            }
        }

        lifecycleScope.launch{
            viewModel.stateOfFunds.collect{
                binding.TotalFunds.text = it + " zł"
            }
        }
        lifecycleScope.launch{
            viewModel.totalPayment.collect{
                binding.paymentValue.text = it + " zł"
            }
        }
        lifecycleScope.launch{
            viewModel.ownPayment.collect{
                binding.ownPaymentValue.text = it + " zł"
            }
        }
        lifecycleScope.launch{
            viewModel.empPayment.collect{
                binding.empPaymentValue.text = it + " zł"
            }
        }
        lifecycleScope.launch{
            viewModel.statePayment.collect{
                binding.countryPaymentValue.text = it + " zł"
            }
        }
        lifecycleScope.launch{
            viewModel.inflationPayment.collect{
                binding.inflationValue.text = it + " zł"
            }
        }
    }

    private fun initChart(ppk: Ppk)
    {
        val lineChart = binding.lineChart

        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.setTouchEnabled(false)

        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(true)
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter(ppk)
        xAxis.setDrawLabels(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f

        val leftAxis: YAxis = lineChart.axisLeft
        leftAxis.textSize = 15f

        //remove right y-axis
        lineChart.axisRight.isEnabled = false

        //remove legend
        lineChart.legend.isEnabled = false


        //remove description label
        lineChart.description.isEnabled = false

        //add animation
        lineChart.animateX(1000, Easing.EaseInSine)

    }

    inner class MyAxisFormatter(private val ppk: Ppk) : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < ppk.dates.size) {
                ppk.dates[index]
            } else {
                ""
            }
        }
    }

    private fun setDataToLineChart(ppk: Ppk) {

        val lineChart = binding.lineChart

        //now draw bar chart with dynamic data
        val entries: ArrayList<Entry> = ArrayList()

        //you can replace this data object with  your custom object
        for (i in 0..(ppk.values.size-1)) {

            entries.add(Entry(i.toFloat(), ppk.values[i].toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "")
        lineDataSet.setColor(R.color.black)
        lineDataSet.setDrawCircles(false)
        val data = LineData(lineDataSet)
        lineChart.data = data

        lineChart.invalidate()
    }
}