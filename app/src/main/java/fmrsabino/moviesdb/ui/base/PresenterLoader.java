package fmrsabino.moviesdb.ui.base;

import android.content.Context;
import android.support.v4.content.Loader;

import rx.functions.Func0;

public class PresenterLoader<T extends Presenter> extends Loader<T> {
    private T presenter;
    private final Func0<T> creator;

    public PresenterLoader(Context context, Func0<T> creator) {
        super(context);
        this.creator = creator;
    }

    @Override
    protected void onStartLoading() {
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        presenter = creator.call();
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.onDestroyed();
        presenter = null;
    }
}
