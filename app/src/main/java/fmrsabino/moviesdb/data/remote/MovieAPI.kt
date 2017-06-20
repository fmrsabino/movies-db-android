package fmrsabino.moviesdb.data.remote

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {
    @GET("movie/{id}")
    fun getMovie(@Path("id") id: Int): Observable<Network.Movie>

    @GET("discover/movie")
    fun discoverMovies(@Query("sort_by") sortBy: String = SORT_BY_POPULARITY_DESC,
                       @Query("language") language: String = "en-US"): Observable<Network.PaginatedResponse<Network.Movie>>

    @GET("discover/tv")
    fun discoverTv(@Query("sort_by") sortBy: String = SORT_BY_POPULARITY_DESC,
                   @Query("language") language: String = "en-US"): Observable<Network.PaginatedResponse<Network.TvSeries>>

    @GET("configuration")
    fun getConfiguration(): Observable<Network.Configuration>

    companion object {
        val SORT_BY_POPULARITY_DESC = "popularity.desc"
    }
}
