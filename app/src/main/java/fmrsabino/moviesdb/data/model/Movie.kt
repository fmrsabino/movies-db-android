package fmrsabino.moviesdb.data.model

import org.threeten.bp.LocalDate

data class Movie(val id: Int,
                 val backdropPath: String? = null,
                 val posterPath: String? = null,
                 val releaseDate: LocalDate? = null,
                 val imdbId: String? = null,
                 val title: String? = null,
                 val tagline: String? = null,
                 val overview: String? = null)
