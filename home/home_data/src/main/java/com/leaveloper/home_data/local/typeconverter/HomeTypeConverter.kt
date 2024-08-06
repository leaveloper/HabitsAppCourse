package com.leaveloper.home_data.local.typeconverter

import android.util.Log
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter

@ProvidedTypeConverter
class HomeTypeConverter() {
    @TypeConverter
    fun fromFrequency(days: List<Int>) : String {
        return joinIntIntoString(days) ?: ""
    }

    @TypeConverter
    fun toFrequency(value: String): List<Int> {
        return splitToIntList(value) ?: emptyList()
    }

    @TypeConverter
    fun fromCompletedDates(days: List<Long>) : String {
        return joinLongIntoString(days) ?: ""
    }

    @TypeConverter
    fun toCompletedDates(value: String): List<Long> {
        return splitToLongList(value) ?: emptyList()
    }

    private fun splitToLongList(input: String?): List<Long>? {
        return input?.split(',')?.mapNotNull { item ->
            try {
                item.toLong()
            } catch (ex: NumberFormatException) {
                Log.e("ROOM", "Malformed integer list", ex)
                null
            }
        }
    }

    private fun splitToIntList(input: String?): List<Int>? {
        return input?.split(',')?.mapNotNull { item ->
            try {
                item.toInt()
            } catch (ex: NumberFormatException) {
                Log.e("ROOM", "Malformed integer list", ex)
                null
            }
        }
    }

    private fun joinIntIntoString(input: List<Int>?): String? {
        return input?.joinToString(",")
    }

    private fun joinLongIntoString(input: List<Long>?): String? {
        return input?.joinToString(",")
    }
}