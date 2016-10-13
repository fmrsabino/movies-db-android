package fmrsabino.moviesdb.data.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import fmrsabino.moviesdb.data.local.table.ImageTable;
import fmrsabino.moviesdb.data.local.table.MovieTable;
import fmrsabino.moviesdb.data.model.configuration.Image;
import fmrsabino.moviesdb.data.model.movie.Movie;
import rx.Observable;
import rx.schedulers.Schedulers;

@Singleton
public class DatabaseHelper {

    private final BriteDatabase db;

    @Inject
    public DatabaseHelper(DbOpenHelper dbOpenHelper) {
        db = SqlBrite.create().wrapDatabaseHelper(dbOpenHelper, Schedulers.io());
    }

    public Observable<Image> setImage(final Image newImage) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        long result;
        try {
            db.delete(ImageTable.TABLE_NAME, null);
            result = db.insert(ImageTable.TABLE_NAME,
                    ImageTable.toContentValues(newImage),
                    SQLiteDatabase.CONFLICT_REPLACE);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        return result >= 0 ? Observable.just(newImage) : Observable.empty();
    }


    public boolean hasImage() {
        Cursor c = db.query("SELECT * FROM " + ImageTable.TABLE_NAME + " LIMIT 1");
        return c.getCount() > 0;
    }

    public Observable<Image> observeImage() {
        return db.createQuery(
                    ImageTable.TABLE_NAME,
                    "SELECT * FROM " + ImageTable.TABLE_NAME + " LIMIT 1")
                .mapToOne(ImageTable::parseCursor);
    }

    public Observable<List<Movie>> observeMovie(String movieId) {
        return db.createQuery(
                MovieTable.TABLE_NAME,
                "SELECT * FROM " + MovieTable.TABLE_NAME + " WHERE id=? LIMIT 1", movieId)
                .mapToList(MovieTable::parseCursor);
    }

    public Observable<Movie> storeMovie(final Movie newMovie) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        long result;
        try {
            result = db.insert(MovieTable.TABLE_NAME,
                    MovieTable.toContentValues(newMovie),
                    SQLiteDatabase.CONFLICT_REPLACE);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        return result >= 0 ? Observable.just(newMovie) : Observable.empty();
    }

    public Observable<String> deleteMovie(final String movieId) {
        BriteDatabase.Transaction transaction = db.newTransaction();
        long result;
        try {
            result = db.delete(MovieTable.TABLE_NAME, "id=?", movieId);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
        return result > 0 ? Observable.just(movieId) : Observable.empty();
    }

    public boolean hasMovie(final String movieId) {
        Cursor c = db.query("SELECT * FROM " + MovieTable.TABLE_NAME + " WHERE id=? LIMIT 1", movieId);
        return c.getCount() > 0;
    }

    public BriteDatabase getDb() {
        return db;
    }
}
