package fmrsabino.moviesdb.data.remote

import fmrsabino.moviesdb.data.model.configuration.Configuration
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.data.model.search.Search
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    @GET("configuration")
    fun getConfiguration(): Observable<Configuration>

    @GET("search/movie")
    fun getSearch(@Query("query") query: String): Observable<Search>

    @GET("movie/{id}")
    fun getMovie(@Path("id") id: String): Observable<Movie>

    @GET("discover/movie")
    fun discoverMovies(@Query("sort_by") sortBy: String = SORT_BY_POPULARITY_DESC,
                       @Query("language") language: String = "en-US"): Observable<Network.PaginatedResponse<Network.Movie>>

    @GET("discover/tv")
    fun discoverTv(@Query("sort_by") sortBy: String = SORT_BY_POPULARITY_DESC,
                   @Query("language") language: String = "en-US"): Observable<Network.PaginatedResponse<Network.TvSeries>>

    @GET("configuration")
    fun getConfigurationNew(): Observable<Network.Configuration>

    companion object {
        val SORT_BY_POPULARITY_DESC = "popularity.desc"
    }
}
