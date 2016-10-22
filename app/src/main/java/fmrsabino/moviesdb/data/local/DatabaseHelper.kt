package fmrsabino.moviesdb.data.local

import android.database.sqlite.SQLiteDatabase
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import fmrsabino.moviesdb.data.local.table.ImageTable
import fmrsabino.moviesdb.data.local.table.MovieTable
import fmrsabino.moviesdb.data.model.configuration.Image
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.util.hasEntries
import fmrsabino.moviesdb.util.inTransaction
import rx.Observable
import rx.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseHelper @Inject constructor(dbOpenHelper: DbOpenHelper) {
    val db: BriteDatabase = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io())

    fun setImage(newImage: Image?): Observable<Image>? {
        db.inTransaction {
            delete(ImageTable.TABLE_NAME, null)
            newImage?.let {
                insert(ImageTable.TABLE_NAME,
                        ImageTable.toContentValues(it),
                        SQLiteDatabase.CONFLICT_REPLACE)
            }
        }
        return Observable.just(newImage)
    }

    fun hasImage(): Boolean = db.hasEntries("SELECT * FROM " + ImageTable.TABLE_NAME + " LIMIT 1")

    fun observeImage(): Observable<Image> {
        return db.createQuery(
                ImageTable.TABLE_NAME,
                "SELECT * FROM " + ImageTable.TABLE_NAME + " LIMIT 1")
                .mapToOne { ImageTable.parseCursor(it) }
    }

    fun observeMovie(movieId: String): Observable<List<Movie>> {
        return db.createQuery(
                MovieTable.TABLE_NAME,
                "SELECT * FROM " + MovieTable.TABLE_NAME + " WHERE id=? LIMIT 1", movieId)
                .mapToList { MovieTable.parseCursor(it) }
    }

    fun storeMovie(newMovie: Movie): Observable<Movie>? {
        db.inTransaction {
            insert(MovieTable.TABLE_NAME,
                    MovieTable.toContentValues(newMovie),
                    SQLiteDatabase.CONFLICT_REPLACE)
        }
        return Observable.just(newMovie)
    }

    fun deleteMovie(movieId: String): Observable<String> {
        db.inTransaction {
            db.delete(MovieTable.TABLE_NAME, "id=?", movieId).toLong()
        }
        return Observable.just(movieId)
    }

    fun hasMovie(movieId: String): Boolean =
            db.hasEntries("SELECT * FROM " + MovieTable.TABLE_NAME + " WHERE id=? LIMIT 1", movieId)
}
