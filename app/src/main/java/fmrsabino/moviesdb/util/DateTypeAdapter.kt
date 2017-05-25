package fmrsabino.moviesdb.util

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class DateTypeAdapter {
    @ToJson fun toJson(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @FromJson internal fun fromJson(date: String): LocalDateTime? {
        for (format in formats) {
            try {
                return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format))
            } catch (e: IllegalArgumentException) {
                Timber.e(e)
            }
        }
        return null
    }

    companion object {
        private val formats = arrayOf("yyyy-MM-dd")
    }
}
