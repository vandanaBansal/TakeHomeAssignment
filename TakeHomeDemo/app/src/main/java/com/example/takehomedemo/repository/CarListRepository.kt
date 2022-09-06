package com.example.takehomedemo.repository

import android.content.Context
import com.example.takehomedemo.database.CarListDao
import com.example.takehomedemo.database.CarRoomDb
import com.example.takehomedemo.model.CarDbModel
import com.example.takehomedemo.model.CarListModel
import com.example.takehomedemo.model.CarProsCons
import com.example.takehomedemo.utils.AppUtil
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class CarListRepository {
    private lateinit var carListDao: CarListDao
    companion object {
        private var INSTANCE: CarListRepository? = null
        fun getRepoInstance() = INSTANCE
            ?: CarListRepository().also {
                INSTANCE = it
            }
    }

    /**
     * fetch data from database
     */
    suspend fun fetchDataFromDb(context: Context): MutableList<CarDbModel>{
        carListDao = CarRoomDb.getAppDatabase(context)!!.carListDao()
        val listLocalDB: Deferred<MutableList<CarDbModel>> =
            CoroutineScope(Dispatchers.IO).async(Dispatchers.IO) {
                carListDao.getAll()
            }
        if (listLocalDB.await().isNotEmpty()) {
            return listLocalDB.await()
        }
        return fetchDataFromAssert(context)
    }

    /**
     * fetch data from assert folder
     */
     private suspend fun fetchDataFromAssert(context: Context): MutableList<CarDbModel> {
        val carList: Deferred<MutableList<CarDbModel>> =
            CoroutineScope(Dispatchers.IO).async(Dispatchers.IO) {
                val jsonString = AppUtil.fetchCarList(context)
                val listCarType = object : TypeToken<ArrayList<CarListModel>>() {}.type
                val mCarDatabaseList = ArrayList<CarDbModel>()
                val mCarDataList: ArrayList<CarListModel> = AppUtil.getCustomGson()
                    .fromJson(jsonString, listCarType)
                // create data as per requirement for expandable recyclerview
                for (item in mCarDataList.indices) {
                    val carPros = ArrayList<CarProsCons>()
                    carPros.add(
                        CarProsCons(
                            label = "Pros:",
                            car_pros = mCarDataList[item].prosList
                        )
                    )
                    carPros.add(
                        CarProsCons(
                            label = "Cons:",
                            car_pros = mCarDataList[item].consList
                        )
                    )
                    mCarDatabaseList.add(
                        CarDbModel(
                            mCarDataList[item].model,
                            mCarDataList[item].make,
                            AppUtil.getDrawableImage(item),
                            mCarDataList[item].marketPrice,
                            mCarDataList[item].rating,
                            0,
                            false,
                            carPros
                        )
                    )
                }
                carListDao.insertAll(mCarDatabaseList) // Insert data in the local DB
                return@async mCarDatabaseList
            }
        return carList.await()
    }
}