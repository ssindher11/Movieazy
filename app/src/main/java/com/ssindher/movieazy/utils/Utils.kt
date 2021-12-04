package com.ssindher.movieazy.utils

import android.content.res.Resources
import android.util.TypedValue
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getFormattedDate(date: Date): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(date)
    }

    fun getPrettyDate(s: String): String {
        val inSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outSdf = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
        return outSdf.format(inSdf.parse(s))
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

    /**
     * Takes time in minutes and returns in hrs and mins
     */
    fun getHrMin(t: Int): String {
        if (t == 0) return ""
        val hrs = t / 60
        val min = t % 60
        return when {
            hrs == 0 && min == 0 -> "0 min"
            hrs == 0 && min != 0 -> "$min ${if (min > 1) "mins" else "min"}"
            hrs != 0 && min == 0 -> "$hrs ${if (hrs > 1) "hrs" else "hr"}"
            else -> "$hrs ${if (hrs > 1) "hrs" else "hr"} $min ${if (min > 1) "mins" else "min"}"
        }
    }

    inline val Int.dp: Int
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics).toInt()
}