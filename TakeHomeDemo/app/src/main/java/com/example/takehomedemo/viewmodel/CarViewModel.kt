package com.example.takehomedemo.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.takehomedemo.model.CarDbModel
import com.example.takehomedemo.repository.CarListRepository
import kotlinx.coroutines.launch

class CarViewModel: ViewModel() {
    private val carListJson = MutableLiveData<MutableList<CarDbModel>>()
    /**
     * fetch the data from the json file
     */
    fun fetchCarListData(context: Context) {
        viewModelScope.launch {
            carListJson.value = CarListRepository.getRepoInstance().fetchDataFromDb(context)
        }
    }
    /**
     * send the car list data
     */
    fun getCarList(): LiveData<MutableList<CarDbModel>> {
        return carListJson
    }
}