package fmrsabino.moviesdb.ui.search

import android.content.Context
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.data.model.search.Search
import fmrsabino.moviesdb.ui.base.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SearchPresenter(context: Context) : BasePresenter<SearchMvpView>(context) {
    private var view: SearchMvpView? = null
    @Inject lateinit var dataManager: DataManager

    private var searchResults: Search? = null
    var previousQuery: String? = null

    private val disposables = CompositeDisposable()

    init {
        inject()
    }

    public override fun onViewAttached(view: SearchMvpView) {
        this.view = view
    }

    public override fun onViewDetached() {
        this.view = null
        disposables.clear()
    }

    override fun onDestroyed() {}

    override fun inject() {
        presenterComponent.inject(this)
    }

    fun loadPosterImageUrl() {
        if (dataManager.hasImage()) {
            dataManager.syncImage()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribeBy(onError = { Timber.e(it) })
        }
        disposables.add(observeDbImage())
    }

    private fun observeDbImage(): Disposable {
        return dataManager.observeImage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribeBy(
                        onNext = {
                            val s = it.logoSizes?.filter { s -> s == "w185" }?.firstOrNull()
                            view?.getPosterImageUrl(it.baseUrl + s)
                        },
                        onError = { Timber.e("Error loading image url") }
                )
    }

    fun getSearch(query: String) {
        if (query == previousQuery) {
            Timber.i("Returning previous search")
            searchResults?.let { view?.showSearchResults(it, false) }
        } else {
            Timber.i("No search done. Fetching from web")
            disposables.add(performSearch(query))
        }
    }

    private fun performSearch(query: String) = dataManager.getRemoteSearch(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                    onNext = {
                        searchResults = it
                        previousQuery = query
                        view?.showSearchResults(it, true)
                    },
                    onError = {
                        Timber.e("Error performing search")
                        this.previousQuery = ""
                        val emptySearch = Search()
                        view?.showSearchResults(emptySearch, true)
                    })
}
