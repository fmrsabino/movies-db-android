package fmrsabino.moviesdb.injection.component;

import dagger.Component;
import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.injection.module.ApplicationModule;
import fmrsabino.moviesdb.injection.scope.PerPresenter;
import fmrsabino.moviesdb.ui.main.MainPresenter;

@PerPresenter
@Component(dependencies = ApplicationComponent.class, modules = ApplicationModule.class)
public interface PresenterComponent {
    void inject(MainPresenter mainPresenter);

    DataManager dataManager();
}
