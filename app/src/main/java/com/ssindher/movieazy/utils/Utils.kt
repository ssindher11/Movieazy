package com.ssindher.movieazy.utils

import android.view.View
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getFormattedDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

    fun getErrorMessage(e: Throwable): String {
        return if (e is HttpException) {
            val error = e
            val errorBody = error.response()?.errorBody()!!.string()
            try {
                val json = JSONObject(errorBody)
                json.getString("message")
            } catch (e: Exception) {
                "Something went wrong"
            }
        } else {
            e.printStackTrace()
            e.localizedMessage ?: ""
        }
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.hide() {
        this.visibility = View.GONE
    }
}