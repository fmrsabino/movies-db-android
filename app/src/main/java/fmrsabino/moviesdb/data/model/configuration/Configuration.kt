package fmrsabino.moviesdb.data.model.configuration

import com.squareup.moshi.Json

data class Configuration(val images: Image? = null)

data class Image(
        @Json(name = "base_url") val baseUrl: String? = null,
        @Json(name = "secure_base_url") val secureBaseUrl: String? = null,
        @Json(name = "backdrop_sizes") val backdropSizes: List<String>? = null,
        @Json(name = "poster_sizes") val posterSizes: List<String>? = null,
        @Json(name = "logo_sizes") val logoSizes: List<String>? = null)
