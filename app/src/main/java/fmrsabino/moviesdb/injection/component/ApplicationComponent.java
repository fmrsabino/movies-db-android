package fmrsabino.moviesdb.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.injection.module.ApplicationModule;
import fmrsabino.moviesdb.injection.scope.ApplicationContext;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    Application application();
    @ApplicationContext Context context();
    DataManager dataManager();
}
