package fmrsabino.moviesdb.injection.module

import android.app.Application
import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.picasso.Picasso
import dagger.Module
import dagger.Provides
import fmrsabino.moviesdb.BuildConfig
import fmrsabino.moviesdb.data.remote.MovieAPI
import fmrsabino.moviesdb.injection.scope.ApplicationContext
import fmrsabino.moviesdb.util.Constants
import fmrsabino.moviesdb.util.DateTypeAdapter
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {
    @Provides
    @Singleton
    @ApplicationContext
    internal fun provideContext(): Context {
        return application
    }

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    internal fun provideMovieService(): MovieAPI {
        val interceptor = Interceptor {
            var request = it.request()
            val builder = request.url().newBuilder()
            val url = builder.addQueryParameter("api_key", BuildConfig.MOVIES_DB_API_KEY).build()
            request = request.newBuilder().url(url).build()
            it.proceed(request)
        }


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

    @Provides
    @Singleton
    internal fun providePicasso(): Picasso {
        return Picasso.with(application)
    }
}
