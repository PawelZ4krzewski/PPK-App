package com.example.ubi.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ubi.R
import com.example.ubi.database.Ppk

class PpkRvAdapter(
    val ppkList: ArrayList<Ppk>
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
    }

    override fun getItemCount(): Int = ppkList.size

    class PpkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ppkName = itemView.findViewById<TextView>(R.id.ppk_name)
        val ppkValue = itemView.findViewById<TextView>(R.id.ppk_value)
        val rvLayout = itemView.findViewById<LinearLayout>(R.id.rvLayout)
        val expandedLayout = itemView.findViewById<LinearLayout>(R.id.expandedLayout)
    }
}