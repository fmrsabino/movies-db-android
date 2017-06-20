package fmrsabino.moviesdb.ui.detail

import fmrsabino.moviesdb.data.model.Movie
import fmrsabino.moviesdb.ui.base.results.Result

data class MovieResult(val movie: Movie? = null,
                       val inProgress: Boolean = false,
                       val error: Throwable? = null): Result