package fmrsabino.moviesdb.ui.detail

import android.content.Context
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.data.model.configuration.Image
import fmrsabino.moviesdb.data.model.movie.Movie
import fmrsabino.moviesdb.ui.base.BasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.lang.kotlin.FunctionSubscriber
import rx.lang.kotlin.plusAssign
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class DetailPresenter internal constructor(context: Context) : BasePresenter<DetailMvpView>(context) {
    private var view: DetailMvpView? = null
    @Inject lateinit var dataManager: DataManager

    private val subscriptions = CompositeSubscription()

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
        subscriptions.clear()
    }

    public override fun onDestroyed() {}

    override fun inject() {
        presenterComponent.inject(this)
    }

    fun loadImageUrl() {
        if (!dataManager.hasImage()) {
            subscriptions += syncRemoteImage()
        }
        subscriptions += observeDbImage()
    }

    private fun syncRemoteImage(): Subscription {
        return dataManager.syncImage()
                .subscribeOn(Schedulers.io())
                .subscribe(FunctionSubscriber<Image>()
                        .onError{ it.printStackTrace() })
    }

    private fun observeDbImage(): Subscription {
        return dataManager.observeImage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(FunctionSubscriber<Image>()
                        .onNext {
                            val posterSize = it.logoSizes?.filter { it == "w185" }?.firstOrNull()
                            posterUrl = it.baseUrl + posterSize
                            posterUrl?.let { view?.getPosterUrl(it) }
                            val coverSize = it.backdropSizes?.filter { it == "w1280" }?.firstOrNull()
                            coverUrl = it.baseUrl + coverSize
                            coverUrl?.let { view?.getCoverUrl(it) } }
                        .onError{ it.printStackTrace() })
    }

    internal fun getMovie(id: String) {
        val activeMovie = this.activeMovie?.copy()
        if (activeMovie != null) {
            view?.getMovieDetails(activeMovie)
        } else if (!dataManager.hasMovie(id)) {
            subscriptions += getRemoteMovie(id)
        }
        subscriptions += observeDbMovie(id)
    }

    private fun getRemoteMovie(id: String): Subscription {
        return dataManager.getRemoteMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(FunctionSubscriber<Movie>()
                        .onNext {
                            activeMovie = it
                            view?.getMovieDetails(it) })
    }

    private fun observeDbMovie(movieId: String): Subscription {
        return dataManager.observeMovie(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(FunctionSubscriber<List<Movie>>()
                        .onNext { movies -> movies.firstOrNull()?.let { view?.savedMovie(it) } })
    }

    fun saveMovie(movie: Movie) {
        dataManager.storeMovie(movie)?.subscribe(FunctionSubscriber<Movie>()
                .onError { it.printStackTrace() })
    }

    internal fun deleteMovie(movieId: String) {
        dataManager.deleteMovie(movieId).subscribe(FunctionSubscriber<String>()
                        .onError { it.printStackTrace() })
    }
}

