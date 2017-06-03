package fmrsabino.moviesdb.data.db

import android.arch.persistence.room.TypeConverter

class DbConverters {
    companion object {
        const val DELIMITER = ','
    }

    @TypeConverter
    fun listToString(list: List<String>) = list.reduce { s1, s2 -> "$s1$DELIMITER$s2" }

    @TypeConverter
    fun stringToList(string: String) = string.split(DELIMITER)
}
