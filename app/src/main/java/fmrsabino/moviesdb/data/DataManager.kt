package fmrsabino.moviesdb.data

import fmrsabino.moviesdb.data.local.DatabaseHelper
import fmrsabino.moviesdb.data.model.configuration.Configuration
import fmrsabino.moviesdb.data.model.configuration.Image
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.data.model.search.Search
import fmrsabino.moviesdb.data.remote.MovieAPI
import rx.Observable
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

    fun observeImage(): Observable<Image> {
        return databaseHelper.observeImage()
    }

    fun hasImage(): Boolean {
        return databaseHelper.hasImage()
    }

    fun syncMovie(id: String): Observable<Movie> {
        return movieService.getMovie(id)
                .concatMap { databaseHelper.storeMovie(it) }
                .doOnNext { Timber.i("Synced movie (%s)", it.title) }
    }

    fun storeMovie(movie: Movie): Observable<Movie>? {
        return databaseHelper.storeMovie(movie)
    }

    fun hasMovie(movieId: String): Boolean {
        return databaseHelper.hasMovie(movieId)
    }

    fun observeMovie(movieId: String): Observable<List<Movie>> {
        return databaseHelper.observeMovie(movieId)
    }

    fun deleteMovie(movieId: String): Observable<String> {
        return databaseHelper.deleteMovie(movieId)
                .doOnNext { id -> Timber.d("Deleted movie with id %s", id) }
    }

    fun getRemoteMovie(id: String): Observable<Movie> {
        return movieService.getMovie(id)
    }
}
