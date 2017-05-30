package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.data.remote.Network

data class ExploreUiModel(val movies: List<Network.Movie> = emptyList(),
                          val tv: List<Network.TvSeries> = emptyList(),
                          val discoverMoviesInProgress: Boolean = false,
                          val discoverTvInProgress: Boolean = false,
                          val configurationInProgress: Boolean = false,
                          val discoverMoviesError: Throwable? = null,
                          val discoverTvError: Throwable? = null,
                          val configurationError: Throwable? = null,
                          val configuration: Network.ImageConfiguration? = null)
