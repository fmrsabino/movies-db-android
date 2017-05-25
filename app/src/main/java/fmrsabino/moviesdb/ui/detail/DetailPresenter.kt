package fmrsabino.moviesdb.ui.detail

import android.content.Context
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class DetailPresenter internal constructor(context: Context) : BasePresenter<DetailMvpView>(context) {
    private var view: DetailMvpView? = null
    @Inject lateinit var dataManager: DataManager

    private val disposables = CompositeDisposable()

    init {
        inject()
    }

    var activeMovie: Movie? = null
        private set
    var posterUrl: String? = null
        private set
    var coverUrl: String? = null
        private set

    public override fun onViewAttached(view: DetailMvpView) {
        this.view = view
    }

    public override fun onViewDetached() {
        view = null
        disposables.clear()
    }

    public override fun onDestroyed() {}

    override fun inject() {
        presenterComponent.inject(this)
    }

    fun loadImageUrl() {
        if (!dataManager.hasImage()) {
            disposables += syncRemoteImage()
        }
        disposables += observeDbImage()
    }

    private fun syncRemoteImage(): Disposable {
        return dataManager.syncImage()
                .subscribeOn(Schedulers.io())
                .subscribeBy(onError = { Timber.e(it) })
    }

    private fun observeDbImage(): Disposable {
        return dataManager.observeImage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            val posterSize = it.logoSizes?.filter { it == "w185" }?.firstOrNull()
                            posterUrl = it.baseUrl + posterSize
                            posterUrl?.let { view?.getPosterUrl(it) }
                            val coverSize = it.backdropSizes?.filter { it == "w1280" }?.firstOrNull()
                            coverUrl = it.baseUrl + coverSize
                            coverUrl?.let { view?.getCoverUrl(it) }
                        },
                        onError = { Timber.e(it) })
    }

    internal fun getMovie(id: String) {
        val activeMovie = this.activeMovie?.copy()
        if (activeMovie != null) {
            view?.getMovieDetails(activeMovie)
        } else if (!dataManager.hasMovie(id)) {
            disposables += getRemoteMovie(id)
        }
        disposables += observeDbMovie(id)
    }

    private fun getRemoteMovie(id: String) = dataManager.getRemoteMovie(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                    onNext = {
                        activeMovie = it
                        view?.getMovieDetails(it)
                    },
                    onError = { Timber.e(it) })

    private fun observeDbMovie(movieId: String) = dataManager.observeMovie(movieId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                    onNext = { movies -> movies.firstOrNull()?.let { view?.savedMovie(it) } },
                    onError = { Timber.e(it) })

    fun saveMovie(movie: Movie) = dataManager.storeMovie(movie)
            .subscribeBy(onError = { Timber.e(it) })

    internal fun deleteMovie(movieId: String) = dataManager.deleteMovie(movieId)
            .subscribeBy(onError = { Timber.e(it) })
}

