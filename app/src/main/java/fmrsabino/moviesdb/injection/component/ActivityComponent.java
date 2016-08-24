package fmrsabino.moviesdb.injection.component;


import android.content.Context;

import dagger.Component;
import fmrsabino.moviesdb.injection.module.ActivityModule;
import fmrsabino.moviesdb.injection.scope.ActivityContext;
import fmrsabino.moviesdb.injection.scope.PerActivity;
import fmrsabino.moviesdb.ui.detail.DetailActivity;
import fmrsabino.moviesdb.ui.main.MainActivity;

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(MainActivity mainActivity);
    void inject(DetailActivity detailActivity);

    @ActivityContext Context context();
}
