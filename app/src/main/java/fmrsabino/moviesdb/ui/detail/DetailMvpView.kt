package fmrsabino.moviesdb.ui.detail

import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.ui.base.MvpView

interface DetailMvpView : MvpView {
    fun getMovieDetails(movie: Movie)
    fun getCoverUrl(url: String)
    fun getPosterUrl(url: String)
    fun savedMovie(movie: Movie)
}
