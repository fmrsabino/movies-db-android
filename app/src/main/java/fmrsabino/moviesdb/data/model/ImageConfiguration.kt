package fmrsabino.moviesdb.data.model

data class ImageConfiguration(val baseUrl: String? = null,
                              val secureBaseUrl: String? = null,
                              val backdropSizes: List<String>? = null,
                              val posterSizes: List<String>? = null,
                              val logoSizes: List<String>? = null)
