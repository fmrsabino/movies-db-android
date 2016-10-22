package fmrsabino.moviesdb.ui.search

import fmrsabino.moviesdb.data.model.search.Search
import fmrsabino.moviesdb.ui.base.MvpView

interface SearchMvpView : MvpView {
    fun showSearchResults(search: Search, newResults: Boolean)
    fun getPosterImageUrl(posterPath: String)
}
