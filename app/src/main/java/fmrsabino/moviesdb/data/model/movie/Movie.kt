package fmrsabino.moviesdb.data.model.movie

import com.squareup.moshi.Json
import org.threeten.bp.LocalDateTime

data class Movie(
        val id: String,
        @Json(name = "backdrop_path") val backdropPath:String? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "release_date") val releaseDate: LocalDateTime? = null,
        @Json(name = "imdb_id") val imdbId: String? = null,
        val title: String? = null,
        val tagline: String? = null,
        val overview: String? = null)
