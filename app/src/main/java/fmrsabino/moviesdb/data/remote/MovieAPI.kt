package fmrsabino.moviesdb.data.remote

import com.squareup.moshi.Moshi
import fmrsabino.moviesdb.BuildConfig
import fmrsabino.moviesdb.data.model.configuration.Configuration
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.data.model.search.Search
import fmrsabino.moviesdb.util.Constants
import fmrsabino.moviesdb.util.DateTypeAdapter
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
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

    companion object {
        val SORT_BY_POPULARITY_DESC = "popularity.desc"

        fun createMovieService(): MovieAPI {
            val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

            val moshi = Moshi.Builder()
                    .add(DateTypeAdapter())
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()

            return retrofit.create(MovieAPI::class.java)
        }


        val interceptor: Interceptor
            get() {
                return Interceptor {
                    var request = it.request()
                    val builder = request.url().newBuilder()
                    val url = builder.addQueryParameter("api_key", BuildConfig.MOVIES_DB_API_KEY).build()
                    request = request.newBuilder().url(url).build()
                    it.proceed(request)
                }
            }
    }
}
