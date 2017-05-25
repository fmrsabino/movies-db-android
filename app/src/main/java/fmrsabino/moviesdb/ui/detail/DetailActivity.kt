package fmrsabino.moviesdb.ui.detail

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.ui.base.BaseActivity
import fmrsabino.moviesdb.ui.base.PresenterLoader
import kotlinx.android.synthetic.main.activity_detail.*
import rx.Subscription
import javax.inject.Inject

class DetailActivity : BaseActivity(), DetailMvpView, LoaderManager.LoaderCallbacks<DetailPresenter> {
    companion object {
        val MOVIE_ID_EXTRA = "movieId"
    }

    @Inject lateinit var dataManager: DataManager
    @Inject lateinit var presenter: DetailPresenter

    private var id: String? = null
    private var stored: Boolean = false

    private var watchlistButtonClick: Subscription? = null

    override fun onCreateLoader(id: Int, args: Bundle): Loader<DetailPresenter> {
        return PresenterLoader(this) { DetailPresenter(this) }
    }

    override fun onLoadFinished(loader: Loader<DetailPresenter>, presenter: DetailPresenter) {
        this.presenter = presenter
    }

    override fun onLoaderReset(loader: Loader<DetailPresenter>) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportLoaderManager.initLoader(0, Bundle(), this)
        id = intent.getStringExtra(MOVIE_ID_EXTRA)
    }

    override fun onResume() {
        super.onResume()
        activity_detail_add_watchlist.clicks().subscribe({
            presenter.activeMovie?.let {
                if (stored) presenter.deleteMovie(it.id) else presenter.saveMovie(it)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        watchlistButtonClick?.unsubscribe()
    }

    override fun onViewAttached() {
        presenter.onViewAttached(this)
        presenter.loadImageUrl()
        id?.let { presenter.getMovie(it) }
    }

    override fun onViewDetached() {
        presenter.onViewDetached()
    }

    override fun getMovieDetails(movie: Movie) {
        activity_detail_movie_title.text = movie.title
        reloadImages()
    }

    override fun getCoverUrl(url: String) {
        reloadImages()
    }

    override fun getPosterUrl(url: String) {
        reloadImages()
    }

    private fun reloadImages() {
        Picasso.with(this)
                .load(presenter.posterUrl + presenter.activeMovie?.backdropPath)
                .fit()
                .centerCrop()
                .into(activity_detail_movie_cover)
    }

    override fun savedMovie(movie: Movie) {
        stored = true
        activity_detail_add_watchlist.text = getString(if (stored) R.string.watchlist_remove else R.string.watchlist_add)
    }
}
