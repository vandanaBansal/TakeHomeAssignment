package com.example.takehomedemo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.example.takehomedemo.model.CarDbModel
import com.example.takehomedemo.model.ProsConsConverter

@Database(entities = [CarDbModel::class], version = 1)
@TypeConverters(ProsConsConverter::class)
abstract class CarRoomDb :RoomDatabase(){
    abstract fun carListDao():CarListDao
    companion object {
        private var INSTANCE: CarRoomDb? = null

        fun getAppDatabase(context: Context): CarRoomDb? {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context, CarRoomDb::class.java, "app_car-db").build()
            }
            return INSTANCE
        }
    }
}