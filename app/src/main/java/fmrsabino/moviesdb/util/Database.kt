package fmrsabino.moviesdb.util


import android.database.Cursor

fun Cursor.getString(columnName: String): String? {
    return this.getString(this.getColumnIndex(columnName))
}

fun Cursor.getLong(columnName: String): Long {
    return this.getLong(this.getColumnIndex(columnName))
}
