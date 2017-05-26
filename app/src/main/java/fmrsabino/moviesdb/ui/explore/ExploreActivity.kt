package fmrsabino.moviesdb.ui.explore

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.ui.base.uievents.RefreshEvent
import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_explore.*
import timber.log.Timber

class ExploreActivity : AppCompatActivity() {
    lateinit var presenter: ExploreContract.Presenter
    var disposable: Disposable? = null
    val refreshSubject: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    val adapter = ExploreAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ViewModelProviders.of(this).get(ExplorePresenter::class.java)
        setContentView(R.layout.activity_explore)

        swipe_refresh.setOnRefreshListener { refreshSubject.onNext(RefreshEvent()) }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        val events: Observable<UiEvent> = refreshSubject
        disposable = events.compose(presenter.transformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { onUiModel(it) }, onError = { Timber.e(it) })
    }

    private fun onUiModel(uiModel: ExploreUiModel) {
        swipe_refresh.isRefreshing = uiModel.inProgress
        if (!presenter.requested) refreshSubject.onNext(RefreshEvent())
        adapter.onNewItems(uiModel.movies)
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }
}
