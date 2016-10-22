package fmrsabino.moviesdb.injection.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import fmrsabino.moviesdb.data.remote.MovieAPI
import fmrsabino.moviesdb.injection.scope.ApplicationContext
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {
    @Provides
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
        return MovieAPI.createMovieService()
    }
}
