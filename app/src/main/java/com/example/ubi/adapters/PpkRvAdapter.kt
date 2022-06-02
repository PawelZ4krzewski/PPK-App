package com.example.ubi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ubi.R
import com.example.ubi.database.Ppk
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class PpkRvAdapter(
    val ppkList: ArrayList<Ppk>,
    val listener: (Ppk) -> Unit
): RecyclerView.Adapter<PpkRvAdapter.PpkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PpkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ppk, parent, false)
        return PpkViewHolder(view)
    }

    override fun onBindViewHolder(holder: PpkRvAdapter.PpkViewHolder, position: Int) {
        val ppk = ppkList[position]
        holder.ppkName.text = ppk.name
        holder.ppkValue.text = ppk.values[ppk.values.size - 1] + " zł"

        val isVisibile: Boolean = ppk.visibility

        holder.expandedLayout.visibility = if(isVisibile) View.VISIBLE else View.GONE

        holder.rvLayout.setOnClickListener {
            ppk.visibility = !ppk.visibility
            notifyItemChanged(position)
        }

        //Chart
        initChart(holder,ppk)
        setDataToLineChart(holder,ppk)

        holder.chooseButton.setOnClickListener{
            listener.invoke(ppk)
        }
    }

    override fun getItemCount(): Int = ppkList.size

    private fun initChart(holder: PpkRvAdapter.PpkViewHolder, ppk: Ppk)
    {
        val lineChart = holder.chart

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

    private fun setDataToLineChart(holder: PpkRvAdapter.PpkViewHolder, ppk: Ppk) {

        val lineChart = holder.chart

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


    class PpkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ppkName = itemView.findViewById<TextView>(R.id.ppk_name)
        val ppkValue = itemView.findViewById<TextView>(R.id.ppk_value)
        val rvLayout = itemView.findViewById<LinearLayout>(R.id.rvLayout)
        val expandedLayout = itemView.findViewById<LinearLayout>(R.id.expandedLayout)
        val chart = itemView.findViewById<LineChart>(R.id.lineChart)
        val chooseButton = itemView.findViewById<Button>(R.id.choosePPKButton)
    }


}