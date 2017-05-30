package fmrsabino.moviesdb.ui.explore

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.view.View
import fmrsabino.moviesdb.MoviesDbApplication
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.injection.component.DaggerViewComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_explore.*
import timber.log.Timber
import javax.inject.Inject


class ExploreActivity : AppCompatActivity() {
    @Inject lateinit var presenterFactory: ExplorePresenter.Companion.Factory
    @Inject lateinit var moviesAdapter: TrendingMoviesAdapter
    @Inject lateinit var tvAdapter: TrendingTvAdapter
    lateinit var presenter: ExploreContract.Presenter
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        presenter = ViewModelProviders.of(this, presenterFactory).get(ExplorePresenter::class.java)
        setContentView(R.layout.activity_explore)

        discover_movies_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        discover_movies_list.adapter = moviesAdapter
        discover_tv_list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        discover_tv_list.adapter = tvAdapter
        LinearSnapHelper().attachToRecyclerView(discover_movies_list)
        LinearSnapHelper().attachToRecyclerView(discover_tv_list)
    }

    override fun onStart() {
        super.onStart()
        disposable = presenter.observeUiModel()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = { onUiModel(it) }, onError = { Timber.e(it) })
    }

    private fun onUiModel(uiModel: ExploreUiModel) {
        presenter.initialRequest()
        moviesAdapter.onNewConfiguration(uiModel.configuration)
        moviesAdapter.onNewItems(uiModel.movies)
        tvAdapter.onNewConfiguration(uiModel.configuration)
        tvAdapter.onNewItems(uiModel.tv)

        uiModel.configurationError?.let { Timber.e("Configuration error") }
        uiModel.discoverMoviesError?.let { Timber.e("Discover Movies error") }
        uiModel.discoverTvError?.let { Timber.e("Discover TV error") }

        tv_error.visibility = if (uiModel.discoverTvError != null) View.VISIBLE else View.GONE
        movies_error.visibility = if (uiModel.discoverMoviesError != null) View.VISIBLE else View.GONE

        if (uiModel.discoverMoviesInProgress) progress_movies.show() else progress_movies.hide()
        if (uiModel.discoverTvInProgress) progress_tv.show() else progress_tv.hide()
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
