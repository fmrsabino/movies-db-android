package fmrsabino.moviesdb.ui.explore

import fmrsabino.moviesdb.data.remote.Network

data class ExploreUiModel(val movies: List<Network.Movie> = emptyList(),
                          val inProgress: Boolean = false,
                          val error: Throwable? = null)
