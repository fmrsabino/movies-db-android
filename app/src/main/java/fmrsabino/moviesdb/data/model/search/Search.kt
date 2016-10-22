package fmrsabino.moviesdb.data.model.search

import com.squareup.moshi.Json
import org.joda.time.DateTime

data class Search(
        val page: Int? = null,
        val results: List<Result>? = null)

data class Result(
        val id: String,
        val title: String,
        val overview: String? = null,
        @Json(name = "backdrop_path") val backdropPath: String? = null,
        @Json(name = "poster_path") val posterPath: String? = null,
        @Json(name = "release_date") val releaseDate: DateTime? = null)
