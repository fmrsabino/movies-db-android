package fmrsabino.moviesdb.injection.module

import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import fmrsabino.moviesdb.BuildConfig
import fmrsabino.moviesdb.data.db.MoviesDb
import fmrsabino.moviesdb.data.remote.MovieAPI
import fmrsabino.moviesdb.injection.scope.ApplicationContext
import fmrsabino.moviesdb.util.Constants
import fmrsabino.moviesdb.util.DateTypeAdapter
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {
    companion object {
        const val OK_HTTP_CACHE_SIZE = 10 * 1024 * 1024L // 10 Mib
    }

    @Provides
    @Singleton
    @ApplicationContext
    fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideMovieService(okHttpClient: OkHttpClient): MovieAPI {
        val moshi = Moshi.Builder()
                .add(DateTypeAdapter())
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        return retrofit.create(MovieAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideOkhttp(@Named("apiKeyInterceptor") interceptor: Interceptor): OkHttpClient {
        val cache = Cache(application.cacheDir, OK_HTTP_CACHE_SIZE)
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .cache(cache)
                .build()

    }

    @Provides
    @Singleton
    @Named("apiKeyInterceptor")
    fun provideApiKeyInterceptor(): Interceptor {
        return Interceptor {
            var request = it.request()
            val builder = request.url().newBuilder()
            val url = builder.addQueryParameter("api_key", BuildConfig.MOVIES_DB_API_KEY).build()
            request = request.newBuilder().url(url).build()
            it.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun providePicasso(): Picasso {
        return Picasso.with(application)
    }

    @Provides
    @Singleton
    fun provideRoomDb(): MoviesDb {
        return Room.databaseBuilder(application, MoviesDb::class.java, MoviesDb.DB_NAME).build()
    }
}
