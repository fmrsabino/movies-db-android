package fmrsabino.moviesdb.data.local.table;

import android.content.ContentValues;
import android.database.Cursor;

import org.joda.time.DateTime;

import fmrsabino.moviesdb.data.model.movie.Movie;
import fmrsabino.moviesdb.util.CursorHelper;

public abstract class MovieTable {
    public static final String TABLE_NAME = "movie";

    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_IMDB_ID = "imdb_id";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TAGLINE = "tagline";
    public static final String COLUMN_OVERVIEW = "overview";


    public static final String CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, " +
            COLUMN_BACKDROP_PATH + " TEXT, " +
            COLUMN_POSTER_PATH + " TEXT, " +
            COLUMN_RELEASE_DATE + " TEXT, " +
            COLUMN_IMDB_ID + " TEXT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_TAGLINE + " TEXT, " +
            COLUMN_OVERVIEW + " TEXT" +
            " ); ";

    public static ContentValues toContentValues(Movie movie) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BACKDROP_PATH, movie.backdropPath());
        values.put(COLUMN_POSTER_PATH, movie.posterPath());
        DateTime releaseDate = movie.releaseDate();
        if (releaseDate != null) {
            values.put(COLUMN_RELEASE_DATE, releaseDate.toDate().getTime());
        }
        values.put(COLUMN_IMDB_ID, movie.imdbId());
        values.put(COLUMN_ID, movie.id());
        values.put(COLUMN_TITLE, movie.title());
        values.put(COLUMN_TAGLINE, movie.tagline());
        values.put(COLUMN_OVERVIEW, movie.overview());
        return values;
    }

    public static Movie parseCursor(Cursor c) {
        int id = CursorHelper.getInt(c, COLUMN_ID);
        if (id == CursorHelper.NULL_VALUE) {
            return null;
        }

        DateTime releaseDate = null;
        long l = CursorHelper.getLong(c, COLUMN_RELEASE_DATE);
        if (l != CursorHelper.NULL_VALUE) {
            releaseDate = new DateTime(l);
        }

        return Movie.builder()
                .backdropPath(CursorHelper.getString(c, COLUMN_BACKDROP_PATH))
                .posterPath(CursorHelper.getString(c, COLUMN_POSTER_PATH))
                .releaseDate(releaseDate)
                .imdbId(CursorHelper.getString(c, COLUMN_IMDB_ID))
                .id(CursorHelper.getInt(c, COLUMN_ID))
                .title(CursorHelper.getString(c, COLUMN_TITLE))
                .tagline(CursorHelper.getString(c, COLUMN_TAGLINE))
                .overview(CursorHelper.getString(c, COLUMN_OVERVIEW))
                .build();
    }
}
