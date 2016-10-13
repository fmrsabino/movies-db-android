package fmrsabino.moviesdb.ui.base;


import android.content.Context;

import fmrsabino.moviesdb.MoviesDbApplication;
import fmrsabino.moviesdb.injection.component.DaggerPresenterComponent;
import fmrsabino.moviesdb.injection.component.PresenterComponent;

public abstract class BasePresenter<V extends MvpView> {
    protected abstract void onViewAttached(V view);
    protected abstract void onViewDetached();
    protected abstract void onDestroyed();
    protected abstract void inject();

    protected final PresenterComponent presenterComponent;

    protected BasePresenter(Context context) {
        presenterComponent = DaggerPresenterComponent.builder()
                .applicationComponent(MoviesDbApplication.get(context).getComponent())
                .build();
        inject();
    }
}
