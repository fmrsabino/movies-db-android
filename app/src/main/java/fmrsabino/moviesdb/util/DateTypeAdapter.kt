package fmrsabino.moviesdb.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

class DateTypeAdapter {
    @ToJson fun toJson(dateTime: DateTime): String {
        return dateTime.toString()
    }

    @FromJson internal fun fromJson(date: String): DateTime? {
        for (format in formats) {
            try {
                return DateTimeFormat.forPattern(format).parseDateTime(date)
            } catch (e: IllegalArgumentException) {
            }

        }
        return null
    }

    companion object {
        private val formats = arrayOf("yyyy-MM-dd")
    }
}
