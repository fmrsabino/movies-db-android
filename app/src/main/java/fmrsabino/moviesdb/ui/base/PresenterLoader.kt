package fmrsabino.moviesdb.ui.base

import android.content.Context
import android.support.v4.content.Loader

class PresenterLoader<T : BasePresenter<*>>(context: Context, private val creator: () -> T) : Loader<T>(context) {
    private var presenter: T? = null

    override fun onStartLoading() {
        presenter?.let { deliverResult(presenter); return }
        forceLoad()
    }

    override fun onForceLoad() {
        presenter = creator()
        deliverResult(presenter)
    }

    override fun onReset() {
        presenter?.onDestroyed()
        presenter = null
    }
}
