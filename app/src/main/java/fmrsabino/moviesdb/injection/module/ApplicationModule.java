package fmrsabino.moviesdb.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import fmrsabino.moviesdb.data.remote.MovieService;
import fmrsabino.moviesdb.data.remote.RetrofitHelper;
import fmrsabino.moviesdb.injection.scope.ApplicationContext;

@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) { this.application = application; }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @Singleton
    MovieService provideMovieService() {
        return RetrofitHelper.newMovieService();
    }
}
