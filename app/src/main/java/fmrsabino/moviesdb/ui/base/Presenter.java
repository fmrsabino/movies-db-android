package fmrsabino.moviesdb.ui.base;

public interface Presenter<V extends MvpView> {
    void onViewAttached(V view);
    void onViewDetached();
    void onDestroyed();
}
