package fmrsabino.moviesdb.ui.base


import android.content.Context

import fmrsabino.moviesdb.MoviesDbApplication
import fmrsabino.moviesdb.injection.component.DaggerPresenterComponent
import fmrsabino.moviesdb.injection.component.PresenterComponent

abstract class BasePresenter<in V : MvpView> protected constructor(context: Context) {
    protected abstract fun onViewAttached(view: V)
    protected abstract fun onViewDetached()
    internal abstract fun onDestroyed()
    protected abstract fun inject()

    protected val presenterComponent: PresenterComponent

    init {
        presenterComponent = DaggerPresenterComponent.builder()
                .applicationComponent(MoviesDbApplication.get(context).component)
                .build()
    }
}
