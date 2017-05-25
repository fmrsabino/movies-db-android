package fmrsabino.moviesdb.ui.explore

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import fmrsabino.moviesdb.MoviesDbApplication
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.injection.component.DaggerViewComponent
import fmrsabino.moviesdb.ui.base.uievents.RefreshEvent
import fmrsabino.moviesdb.ui.base.uievents.UiEvent
import fmrsabino.moviesdb.ui.base.uievents.ViewActiveEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_explore.*
import timber.log.Timber
import javax.inject.Inject

class ExploreActivity : AppCompatActivity() {
    var disposable: Disposable? = null
    @Inject lateinit var presenter: ExploreContract.Presenter
    @Inject lateinit var adapter: ExploreAdapter
    val refreshSubject: PublishSubject<RefreshEvent> = PublishSubject.create<RefreshEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        setContentView(R.layout.activity_explore)

        swipe_refresh.setOnRefreshListener { refreshSubject.onNext(RefreshEvent()) }
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        refreshSubject.doOnTerminate { Timber.d("TERMINATE CALLED ON SUBJECT") }
    }

    override fun onStart() {
        super.onStart()
        val events: Observable<UiEvent> = Observable.merge(
                Observable.just(ViewActiveEvent()),
                refreshSubject)
        disposable = events.compose(presenter.transformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { onUiModel(it) }, onError = { Timber.e(it) })
    }

    private fun onUiModel(uiModel: ExploreUiModel) {
        swipe_refresh.isRefreshing = uiModel.inProgress
        adapter.onNewItems(uiModel.movies)
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    private fun inject() {
        DaggerViewComponent.builder()
                .applicationComponent(MoviesDbApplication[this].component)
                .build()
                .inject(this)
    }
}
