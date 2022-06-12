package com.example.ubi.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ubi.R
import com.example.ubi.database.Ppk
import com.example.ubi.database.UserPayment
import com.example.ubi.database.payment.Payment
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PaymentRvAdapter(val userPaymentList: ArrayList<UserPayment>,
): RecyclerView.Adapter<PaymentRvAdapter.PaymentRvAdapter>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentRvAdapter {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
        return PaymentRvAdapter(view)
    }

    override fun onBindViewHolder(holder: PaymentRvAdapter, position: Int) {

        Log.d("PaymentRV",userPaymentList.size.toString())
        val userPayment = userPaymentList[position]


        holder.paymentTotal.text = (userPayment.payment.userPayment + userPayment.payment.companyPayment + userPayment.payment.countryPayment).toString() + " zł"
        holder.paymentDate.text = getDateTime(userPayment.payment.date)
        holder.ownPayment.text = userPayment.payment.userPayment.toString() + " zł"
        holder.companyPayment.text = userPayment.payment.companyPayment.toString() + " zł"
        holder.statePayment.text = userPayment.payment.countryPayment.toString() + " zł"
        holder.ppkAmount.text = userPayment.payment.ppkAmount.toString()



        val isVisibile: Boolean = userPayment.visibility

        holder.expandedLayout.visibility = if(isVisibile) View.VISIBLE else View.GONE

        holder.rvLayout.setOnClickListener {
            userPayment.visibility = !userPayment.visibility
            notifyItemChanged(position)
        }

    }

    private fun getDateTime(s: String): String? {
        try{
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val netDate = Date(s.toLong())
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    override fun getItemCount(): Int = userPaymentList.size

    class PaymentRvAdapter(itemView: View) : RecyclerView.ViewHolder(itemView){
        val rvLayout = itemView.findViewById<LinearLayout>(R.id.paymentLayout)
        val expandedLayout = itemView.findViewById<LinearLayout>(R.id.expandedLayout)
        val paymentTotal = itemView.findViewById<TextView>(R.id.ppkTotalValue)
        val paymentDate = itemView.findViewById<TextView>(R.id.ppkDate)
        val ownPayment = itemView.findViewById<TextView>(R.id.ownPaymentValue)
        val companyPayment = itemView.findViewById<TextView>(R.id.empPaymentValue)
        val statePayment = itemView.findViewById<TextView>(R.id.countryPaymentValue)
        val ppkAmount = itemView.findViewById<TextView>(R.id.PpkAmountValue)
    }


}