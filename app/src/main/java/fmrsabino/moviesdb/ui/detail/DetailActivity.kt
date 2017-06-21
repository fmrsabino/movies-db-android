package fmrsabino.moviesdb.ui.detail

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import fmrsabino.moviesdb.MoviesDbApplication
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.injection.component.DaggerViewComponent
import fmrsabino.moviesdb.injection.module.ViewModule
import fmrsabino.moviesdb.util.showToast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_detail.*
import timber.log.Timber
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {
    companion object {
        const val MOVIE_ID_KEY = "prefs.int.movie_id"
        const val MOVIE_TITLE_KEY = "prefs.string.movie.title"
    }

    @Inject lateinit var presenterFactory: DetailPresenter.Companion.Factory
    lateinit var presenter: DetailContract.Presenter
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        presenter = ViewModelProviders.of(this, presenterFactory).get(DetailPresenter::class.java)
        setContentView(R.layout.activity_detail)
        val movieId = intent.getIntExtra(MOVIE_ID_KEY, -1)
        val movieTitle = intent.getStringExtra(MOVIE_TITLE_KEY)
        if (movieId == -1) {
            showToast("Invalid movie id :(")
            finish()
        }
        presenter.setMediaId(movieId)
        setSupportActionBar(toolbar)
        supportActionBar?.title = movieTitle ?: "Unknown title"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onStart() {
        super.onStart()
        disposable = presenter.observeUiModel()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(onNext = this::onUiModel, onError = Timber::e)
    }

    private fun onUiModel(uiModel: DetailUiModel) {
        Timber.d("UiModel: $uiModel")
    }

    override fun onStop() {
        super.onStop()
        disposable?.dispose()
    }

    private fun inject() {
        DaggerViewComponent.builder()
                .viewModule(ViewModule(this))
                .applicationComponent(MoviesDbApplication[this].component)
                .build().inject(this)
    }
}
