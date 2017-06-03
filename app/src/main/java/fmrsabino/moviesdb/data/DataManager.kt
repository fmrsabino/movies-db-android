package fmrsabino.moviesdb.data

import fmrsabino.moviesdb.data.db.MoviesDb
import fmrsabino.moviesdb.data.model.ImageConfiguration
import fmrsabino.moviesdb.data.model.fromDb
import fmrsabino.moviesdb.data.model.toDb
import fmrsabino.moviesdb.data.remote.MovieAPI
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton class DataManager
@Inject constructor(private val movieService: MovieAPI, private val moviesDb: MoviesDb) {
    fun getConfiguration(): Observable<ImageConfiguration> {
        return movieService.getConfiguration()
                .map { it.images?.toDb() }
                .doOnNext { it?.let { moviesDb.imageConfigurationDao().insertImageConfiguration(it) } }
                .flatMap {
                    moviesDb.imageConfigurationDao().getImageConfiguration()
                            .toObservable()
                            .map { it.fromDb() }
                }
    }

    fun getRemoteMovie(id: String) = movieService.getMovie(id)

    fun discoverMovies() = movieService.discoverMovies().map { it.results }

    fun discoverTv() = movieService.discoverTv().map { it.results }
}
