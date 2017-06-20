package fmrsabino.moviesdb.data.remote

import com.squareup.moshi.Json
import org.threeten.bp.LocalDate

object Network {
    data class Movie(@Json(name = "id") val id: Int,
                     @Json(name = "backdrop_path") val backdropPath: String? = null,
                     @Json(name = "poster_path") val posterPath: String? = null,
                     @Json(name = "release_date") val releaseDate: LocalDate? = null,
                     @Json(name = "imdb_id") val imdbId: String? = null,
                     @Json(name = "title") val title: String? = null,
                     @Json(name = "tagline") val tagline: String? = null,
                     @Json(name = "overview") val overview: String? = null)

    data class TvSeries(@Json(name = "id") val id: String,
                        @Json(name = "poster_path") val posterPath: String? = null,
                        @Json(name = "name") val name: String? = null)

    data class PaginatedResponse<out T>(val page: Int, val results: List<T>)

    data class Configuration(@Json(name = "images") val images: ImageConfiguration?)

    data class ImageConfiguration(
            @Json(name = "base_url") val baseUrl: String? = null,
            @Json(name = "secure_base_url") val secureBaseUrl: String? = null,
            @Json(name = "backdrop_sizes") val backdropSizes: List<String>? = null,
            @Json(name = "poster_sizes") val posterSizes: List<String>? = null,
            @Json(name = "logo_sizes") val logoSizes: List<String>? = null)
}
