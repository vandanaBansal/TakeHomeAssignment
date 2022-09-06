package com.example.takehomedemo.utils

import android.content.Context
import android.util.Log
import com.example.takehomedemo.R
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.IOException

class AppUtil {
    companion object {
        fun fetchCarList(context: Context): String {
            lateinit var jsonString: String
            val fileName = "car_list.json"
            try {
                jsonString = context.assets.open(fileName)
                    .bufferedReader()
                    .use { it.readText() }
            } catch (ioException: IOException) {
                Log.d("TAG", ioException.toString())
            }
            return jsonString
        }

        /**
         * get image from drawable folder
         */
        fun getDrawableImage(position: Int): Int {
            val image = when (position) {
                0 -> R.drawable.range_rover
                1 -> R.drawable.alpine_roadster
                2 -> R.drawable.bmw
                3 -> R.drawable.ermcedez_benz_glc
                else -> R.drawable.tacoma
            }
            return image
        }

        /**
         * get gson from gson builder
         */
        fun getCustomGson(): Gson {
            val gb = GsonBuilder()
            return gb.create()
        }
    }
}