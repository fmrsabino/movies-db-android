package fmrsabino.moviesdb.ui.detail

import fmrsabino.moviesdb.data.model.ImageConfiguration
import fmrsabino.moviesdb.data.model.Movie

data class DetailUiModel(val movie: Movie? = null,
                         val movieError: Throwable? = null,
                         val movieInProgress: Boolean = false,
                         val configuration: ImageConfiguration? = null,
                         val configurationInProgress: Boolean = false)