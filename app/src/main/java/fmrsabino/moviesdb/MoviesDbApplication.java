package fmrsabino.moviesdb;

import android.app.Application;
import android.content.Context;

import com.jakewharton.threetenabp.AndroidThreeTen;

import fmrsabino.moviesdb.injection.component.ApplicationComponent;
import fmrsabino.moviesdb.injection.component.DaggerApplicationComponent;
import fmrsabino.moviesdb.injection.module.ApplicationModule;
import timber.log.Timber;

public class MoviesDbApplication extends Application {
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidThreeTen.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static MoviesDbApplication get(Context context) {
        return (MoviesDbApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (applicationComponent == null) {
            applicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return applicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent) {
        this.applicationComponent = applicationComponent;
    }
}
