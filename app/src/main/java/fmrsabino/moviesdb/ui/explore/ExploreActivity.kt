package fmrsabino.moviesdb.ui.explore

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import fmrsabino.moviesdb.MoviesDbApplication
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.injection.component.DaggerViewComponent
import fmrsabino.moviesdb.ui.base.uievents.RefreshEvent
import fmrsabino.moviesdb.util.showSnackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_explore.*
import timber.log.Timber
import javax.inject.Inject


class ExploreActivity : AppCompatActivity() {
    @Inject lateinit var presenterFactory: ExplorePresenter.Companion.Factory
    @Inject lateinit var adapter: ExploreAdapter
    lateinit var presenter: ExploreContract.Presenter
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        presenter = ViewModelProviders.of(this, presenterFactory).get(ExplorePresenter::class.java)
        setContentView(R.layout.activity_explore)

        swipe_refresh.setOnRefreshListener { presenter.uiEvents.onNext(RefreshEvent()) }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        disposable = presenter.observeUiModel()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { onUiModel(it) }, onError = { Timber.e(it) })
    }

    private fun onUiModel(uiModel: ExploreUiModel) {
        swipe_refresh.isRefreshing = uiModel.inProgress
        if (!presenter.requested) presenter.uiEvents.onNext(RefreshEvent())
        uiModel.error?.let {
            Timber.e(it)
            it.message?.let { coordinator.showSnackbar(it) }
        }
        adapter.onNewItems(uiModel.movies)
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    fun inject() {
        DaggerViewComponent.builder()
                .applicationComponent(MoviesDbApplication[this].component)
                .build()
                .inject(this)
    }
}
