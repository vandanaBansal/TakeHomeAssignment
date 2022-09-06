package com.example.takehomedemo.view

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.takehomedemo.adapter.CarAdapter
import com.example.takehomedemo.databinding.ActivityMainBinding
import com.example.takehomedemo.model.CarDbModel
import com.example.takehomedemo.model.FilterModel
import com.example.takehomedemo.viewmodel.CarViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: CarAdapter
    private lateinit var carList: MutableList<CarDbModel>
    private lateinit var viewModel: CarViewModel
    private var filterModel = FilterModel()
    private var makeList = arrayListOf<String>()
    private var modelList = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initView()
    }

    /**
     * initialize the view
     */
    private fun initView() {
        binding.rvCar.layoutManager = LinearLayoutManager(this) //set layout manager
        viewModel = ViewModelProvider(this)[CarViewModel::class.java] // attach view model
        viewModel.fetchCarListData(context = this)                           // call car data from json file or local DB
        viewModel.getCarList().observe(this) {
            if (it.isNotEmpty()) {
                this.carList = it
                adapter = CarAdapter(this, carList) // set data on adapter
                binding.rvCar.adapter = adapter
                setDataOnMakeSpinner()
                setDataOnModelSpinner()
            }
        }
    }

    /**
     * list set on make spinner
     */
    private fun setDataOnMakeSpinner() {
        with(makeList) { add("Any make") }
        for (i in carList.indices) {
            makeList.add(carList[i].car_name!!)
        }
        val makeAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, makeList)
        makeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spAnyMake.adapter = makeAdapter
        binding.spAnyMake.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    filterModel.car_make = ""
                    adapter.filterCarList(carList)
                } else {
                    filterModel.car_make = makeList[position]
                    filterList(filterModel)
                }
            }
        }
    }

    /**
     * list set on model spinner
     */
    private fun setDataOnModelSpinner() {
        with(modelList) { add("Any model") }
        for (i in carList.indices) {
            modelList.add(carList[i].car_model)
        }
        val modelAdapter = ArrayAdapter(this, R.layout.simple_spinner_item, modelList)
        modelAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.spAnyModel.adapter = modelAdapter
        binding.spAnyModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    filterModel.car_model = ""
                    adapter.filterCarList(carList)
                } else {
                    filterModel.car_model = modelList[position]
                    filterList(filterModel)
                }
            }
        }
    }

    /**
     * @Method:  filter the list of cars with make model
     */
    fun filterList(filterModel: FilterModel) {
        val listFiltered = ArrayList<CarDbModel>()
        // car make and car model is not empty
        if (!filterModel.car_make.isNullOrEmpty() && !filterModel.car_model.isNullOrEmpty()) {
            for (item in carList) {
                // match car make and car model for filter
                if (item.car_name != null && item.car_model != null) {
                    if (item.car_name!!.trim().lowercase()
                            .contains(filterModel.car_make!!.trim().lowercase()) &&
                        item.car_model.trim().lowercase()
                            .contains(filterModel.car_model!!.trim().lowercase())
                    ) {
                        listFiltered.add(item)
                    }
                }

            }
        }
        //car make is not empty
        else if (!filterModel.car_make.isNullOrEmpty()) {
            for (item in carList) {
                // match car make for filter
                item.car_name?.let {
                    if (it.trim().lowercase().contains(filterModel.car_make!!.trim().lowercase())) {
                        listFiltered.add(item)
                    }
                }

            }
        }
        //car model is not empty
        else if (!filterModel.car_model.isNullOrEmpty()) {
            for (item in carList) {
                // match car model for filter
                item.car_model?.let {
                    if (it.trim().lowercase()
                            .contains(filterModel.car_model!!.trim().lowercase())
                    ) {
                        listFiltered.add(item)
                    }
                }

            }
        }
        adapter.filterCarList(listFiltered)
    }
}


