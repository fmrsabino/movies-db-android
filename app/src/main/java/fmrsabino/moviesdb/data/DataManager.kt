package fmrsabino.moviesdb.data

import fmrsabino.moviesdb.data.local.DatabaseHelper
import fmrsabino.moviesdb.data.model.configuration.Configuration
import fmrsabino.moviesdb.data.model.configuration.Image
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.data.model.search.Search
import fmrsabino.moviesdb.data.remote.MovieAPI
import io.reactivex.Observable
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class DataManager
@Inject constructor(private val movieService: MovieAPI, private val databaseHelper: DatabaseHelper) {
    fun getRemoteConfiguration(): Observable<Configuration> {
        return movieService.getConfiguration()
    }

    fun getRemoteSearch(query: String): Observable<Search> {
        return movieService.getSearch(query)
    }

    fun syncImage(): Observable<Image> {
        return movieService.getConfiguration()
                .concatMap { databaseHelper.setImage(it.images) }
                .doOnNext { Timber.i("Synced image - %s", it.toString()) }
    }

    fun observeImage() = databaseHelper.observeImage()

    fun hasImage() = databaseHelper.hasImage()

    fun syncMovie(id: String) = movieService.getMovie(id)
            .concatMap { databaseHelper.storeMovie(it) }
            .doOnNext { Timber.i("Synced movie (%s)", it.title) }

    fun storeMovie(movie: Movie) = databaseHelper.storeMovie(movie)

    fun hasMovie(movieId: String) = databaseHelper.hasMovie(movieId)

    fun observeMovie(movieId: String) = databaseHelper.observeMovie(movieId)

    fun deleteMovie(movieId: String) = databaseHelper.deleteMovie(movieId)
            .doOnNext { id -> Timber.d("Deleted movie with id %s", id) }

    fun getRemoteMovie(id: String) = movieService.getMovie(id)
}
