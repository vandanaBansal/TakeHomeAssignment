package com.example.takehomedemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.takehomedemo.R
import com.example.takehomedemo.databinding.CarListItemBinding
import com.example.takehomedemo.databinding.ExpendCarListItemBinding
import com.example.takehomedemo.model.CarDbModel
import com.example.takehomedemo.model.CarProsCons


class CarAdapter(private val context: Context, private var carList: MutableList<CarDbModel>) :
    Adapter<RecyclerView.ViewHolder>() {

    class MainViewHolder(row: CarListItemBinding) : RecyclerView.ViewHolder(row.root) {
        val mItem = row
    }

    class ChildViewHolder(row: ExpendCarListItemBinding) : RecyclerView.ViewHolder(row.root) {
        val mItem = row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val mainItemLayoutBinding =
                CarListItemBinding.inflate(LayoutInflater.from(parent.context))
            MainViewHolder(mainItemLayoutBinding)
        } else {
            val expandableItemLayoutBinding =
                ExpendCarListItemBinding.inflate(LayoutInflater.from(parent.context))
            ChildViewHolder(expandableItemLayoutBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val modelData = carList[position]
        if (modelData.parent_view == 0) {
            holder as MainViewHolder
            holder.apply {
                mItem.tvCarName.text = "${modelData.car_name} ${modelData.car_model}"
                mItem.tvCarPrice.text =
                    "${context.resources.getString(R.string.price)} ${modelData.car_price}"
                mItem.rbCar.rating = modelData.car_rating!!.toFloat()
                mItem.ivCar.setImageResource(modelData.car_image!!)
                mItem.clCarMain.setOnClickListener {
                println()
                    expandOrCollapseParentItem(modelData, position)
                    for (i in carList.indices) {
                        if (carList[i].isExpanded == true && i != position) {
                            collapseParentRow(i)
                            break
                        }
                    }
                }
                if (modelData.isExpanded==true) {
                    mItem.view.visibility = View.GONE
                } else {
                    mItem.view.visibility = View.VISIBLE
                }
            }
        } else {
            holder as ChildViewHolder
            holder.apply {
                for (item in modelData.car_pros_cons!!.indices) {
                    if (!modelData.car_pros_cons!![item].car_pros.isNullOrEmpty()) {
                        mItem.tvProCon.visibility = View.VISIBLE
                        mItem.rvExpendView.visibility = View.VISIBLE
                        mItem.tvProCon.text = modelData.car_pros_cons!![item].label
                        mItem.rvExpendView.layoutManager = LinearLayoutManager(context)
                        mItem.rvExpendView.adapter =
                            ChildProConAdapter(modelData.car_pros_cons!![item].car_pros!!)
                    } else {
                        mItem.tvProCon.visibility = View.GONE
                        mItem.rvExpendView.visibility = View.GONE
                    }
                    if (modelData.car_pros_cons!![item].label.equals("cons:", true)) {
                        mItem.view.visibility = View.VISIBLE
                    } else {
                        mItem.view.visibility = View.GONE
                    }
                }
            }
        }
    }


    // check item if collapse or expand
    private fun expandOrCollapseParentItem(singleBoarding: CarDbModel, position: Int) {
        if (singleBoarding.isExpanded!!) {
            collapseParentRow(position)
        } else {
            expandParentRow(position)
        }
    }

    // expand view handle
    private fun expandParentRow(position: Int) {
        val currentBoardingRow = carList[position]
        val services = currentBoardingRow.car_pros_cons
        currentBoardingRow.isExpanded = true
        var nextPosition = position
        if (currentBoardingRow.parent_view == 0) {
            services?.forEach { service ->
                val parentModel = CarDbModel(car_model = "")
                parentModel.parent_view = 1
                val subList: ArrayList<CarProsCons> = ArrayList()
                subList.add(service)
                parentModel.car_pros_cons = subList
                carList.add(++nextPosition, parentModel)
            }
            notifyDataSetChanged()
        }
    }

    // Collapse view handle
    private fun collapseParentRow(position: Int) {
        val currentBoardingRow = carList[position]
        val services = currentBoardingRow.car_pros_cons
        if (carList[position].isExpanded == true) {
            carList[position].isExpanded = false
            if (carList[position].parent_view == 0) {
                services?.forEach { _ ->
                    carList.removeAt(position + 1)
                }
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return carList.size
    }

    override fun getItemViewType(position: Int): Int = carList[position].parent_view!!


    // list filter and notify
    fun filterCarList(filterList: MutableList<CarDbModel>) {
        carList = filterList
        notifyDataSetChanged()
    }
}

