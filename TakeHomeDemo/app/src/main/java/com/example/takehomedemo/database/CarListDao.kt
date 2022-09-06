package com.example.takehomedemo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.takehomedemo.model.CarDbModel

@Dao
interface CarListDao {
    @Query("SELECT * FROM car_table")
    suspend fun getAll(): MutableList<CarDbModel>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(Courses: List<CarDbModel>)
}