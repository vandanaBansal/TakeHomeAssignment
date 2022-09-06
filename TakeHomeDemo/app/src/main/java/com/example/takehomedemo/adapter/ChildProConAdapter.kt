package com.example.takehomedemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.takehomedemo.databinding.ProConListItemBinding


class ChildProConAdapter(private val mList:List<String>) : Adapter<ChildProConAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProConListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(mList[position])
        }
    }

    override fun getItemCount(): Int {
       return mList.size
    }

   inner class ViewHolder(private val binding: ProConListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mData: String) {
            if (mData.isNotEmpty()) {
                binding.tvPoint.visibility = View.VISIBLE
                binding.tvChildData.visibility = View.VISIBLE
                binding.tvChildData.text = mData
            } else {
                binding.tvPoint.visibility = View.GONE
                binding.tvChildData.visibility = View.GONE
            }
        }
    }
}

