package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.data.remote.Network
import fmrsabino.moviesdb.ui.base.results.Result

data class DiscoverMoviesResult(val movies: List<Network.Movie> = emptyList(),
                                val inProgress: Boolean = false,
                                val error: Throwable? = null) : Result
