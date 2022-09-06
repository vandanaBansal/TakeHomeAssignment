package com.example.takehomedemo.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.takehomedemo.utils.AppUtil
import com.google.gson.reflect.TypeToken

@Entity(tableName = "car_table")
data class CarDbModel(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "car_model")
    var car_model: String,

    @ColumnInfo(name = "car_name")
    var car_name: String? = null,

    @ColumnInfo(name = "car_image")
    var car_image: Int? = null,

    @ColumnInfo(name = "car_price")
    var car_price: Double? = 0.0,

    @ColumnInfo(name = "car_rating")
    val car_rating: Int? = 0,

    @ColumnInfo(name = "parent_view")
    var parent_view: Int? = null,

    @ColumnInfo(name = "isExpanded")
    var isExpanded: Boolean? = false,


    @ColumnInfo(name = "car_pros_cons")
    var car_pros_cons: ArrayList<CarProsCons>? = null
)
class ProsConsConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<CarProsCons> {
        val listType = object : TypeToken<ArrayList<CarProsCons>>() {}.type
        return AppUtil.getCustomGson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArray(list: ArrayList<CarProsCons>): String {
        return AppUtil.getCustomGson().toJson(list)
    }
}
