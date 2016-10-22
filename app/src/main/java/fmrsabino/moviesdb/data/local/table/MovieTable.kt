package fmrsabino.moviesdb.data.local.table

import android.content.ContentValues
import android.database.Cursor
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.util.getLong
import fmrsabino.moviesdb.util.getString
import org.joda.time.DateTime

object MovieTable {
    val TABLE_NAME = "movie"

    val COLUMN_BACKDROP_PATH = "backdrop_path"
    val COLUMN_POSTER_PATH = "poster_path"
    val COLUMN_RELEASE_DATE = "release_date"
    val COLUMN_IMDB_ID = "imdb_id"
    val COLUMN_ID = "id"
    val COLUMN_TITLE = "title"
    val COLUMN_TAGLINE = "tagline"
    val COLUMN_OVERVIEW = "overview"


    val CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " TEXT NOT NULL PRIMARY KEY, " +
            COLUMN_BACKDROP_PATH + " TEXT, " +
            COLUMN_POSTER_PATH + " TEXT, " +
            COLUMN_RELEASE_DATE + " TEXT, " +
            COLUMN_IMDB_ID + " TEXT, " +
            COLUMN_TITLE + " TEXT, " +
            COLUMN_TAGLINE + " TEXT, " +
            COLUMN_OVERVIEW + " TEXT" +
            " ); "

    fun toContentValues(movie: Movie): ContentValues {
        val values = ContentValues()
        values.put(COLUMN_ID, movie.id)
        movie.backdropPath?.let { values.put(COLUMN_BACKDROP_PATH, movie.backdropPath) }
        movie.posterPath?.let { values.put(COLUMN_POSTER_PATH, movie.posterPath) }
        movie.releaseDate?.let { values.put(COLUMN_RELEASE_DATE, movie.releaseDate.toDate().time) }
        movie.imdbId?.let { values.put(COLUMN_IMDB_ID, movie.imdbId) }
        movie.title?.let { values.put(COLUMN_TITLE, movie.title) }
        movie.tagline?.let { values.put(COLUMN_TAGLINE, movie.tagline) }
        movie.overview?.let { values.put(COLUMN_OVERVIEW, movie.overview) }
        return values
    }

    fun parseCursor(c: Cursor): Movie {
        return Movie(
                id = c.getString(COLUMN_ID)!!,
                backdropPath = c.getString(COLUMN_BACKDROP_PATH),
                posterPath = c.getString(COLUMN_POSTER_PATH),
                releaseDate = DateTime(c.getLong(COLUMN_RELEASE_DATE)),
                imdbId = c.getString(COLUMN_IMDB_ID),
                title = c.getString(COLUMN_TITLE),
                tagline = c.getString(COLUMN_TAGLINE),
                overview = c.getString(COLUMN_OVERVIEW))
    }
}
