package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.data.model.ImageConfiguration
import fmrsabino.moviesdb.data.remote.Network
import fmrsabino.moviesdb.ui.base.results.Result

data class DiscoverMoviesResult(val movies: List<Network.Movie> = emptyList(),
                                val inProgress: Boolean = false,
                                val error: Throwable? = null) : Result

data class DiscoverTvResult(val series: List<Network.TvSeries> = emptyList(),
                            val inProgress: Boolean = false,
                            val error: Throwable? = null) : Result


data class ConfigurationResult(
        val imageConfiguration: ImageConfiguration? = null,
        val inProgress: Boolean = false,
        val error: Throwable? = null) : Result
