package fmrsabino.moviesdb.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import fmrsabino.moviesdb.data.local.table.ImageTable
import fmrsabino.moviesdb.data.local.table.MovieTable
import fmrsabino.moviesdb.injection.scope.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DbOpenHelper
@Inject
constructor(@ApplicationContext context: Context) :
        SQLiteOpenHelper(context, DbOpenHelper.DATABASE_NAME, null, DbOpenHelper.DATABASE_VERSION) {

    companion object {
        val DATABASE_NAME = "movies.db"
        val DATABASE_VERSION = 1
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.beginTransaction()
        try {
            db.execSQL(ImageTable.CREATE)
            db.execSQL(MovieTable.CREATE)
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}
