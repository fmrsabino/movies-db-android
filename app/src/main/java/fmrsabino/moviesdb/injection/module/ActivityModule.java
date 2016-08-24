package fmrsabino.moviesdb.injection.module;

import android.app.Activity;
import android.content.Context;

import dagger.Module;
import dagger.Provides;
import fmrsabino.moviesdb.injection.scope.ActivityContext;
import fmrsabino.moviesdb.injection.scope.PerActivity;

@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @PerActivity
    @ActivityContext
    Context provideContext() { return activity; }
}
