package fmrsabino.moviesdb.injection.component;

import dagger.Component;
import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.injection.module.ApplicationModule;
import fmrsabino.moviesdb.injection.scope.PerPresenter;
import fmrsabino.moviesdb.ui.detail.DetailPresenter;
import fmrsabino.moviesdb.ui.search.SearchPresenter;

@PerPresenter
@Component(dependencies = ApplicationComponent.class, modules = ApplicationModule.class)
public interface PresenterComponent {
    void inject(SearchPresenter mainPresenter);
    void inject(DetailPresenter detailPresenter);

    DataManager dataManager();
}
