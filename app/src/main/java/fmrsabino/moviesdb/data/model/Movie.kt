package fmrsabino.moviesdb.data.model

import org.threeten.bp.LocalDateTime

data class Movie(val id: String,
                 val backdropPath: String? = null,
                 val posterPath: String? = null,
                 val releaseDate: LocalDateTime? = null,
                 val imdbId: String? = null,
                 val title: String? = null,
                 val tagline: String? = null,
                 val overview: String? = null)
