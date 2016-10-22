package fmrsabino.moviesdb.util


import android.database.Cursor
import com.squareup.sqlbrite.BriteDatabase

fun Cursor.getString(columnName: String): String? {
    return this.getString(this.getColumnIndex(columnName))
}

fun Cursor.getLong(columnName: String): Long {
    return this.getLong(this.getColumnIndex(columnName))
}

inline fun BriteDatabase.inTransaction(func: BriteDatabase.() -> Unit) {
    val transaction = newTransaction()
    try {
        func()
        transaction.markSuccessful()
    } finally {
        transaction.end()
    }
}

fun BriteDatabase.countQuery(query: String, vararg args: String): Int {
    this.query(query, *args).use { return it.count }
}

fun BriteDatabase.hasEntries(query: String, vararg args: String): Boolean = countQuery(query, *args) > 0