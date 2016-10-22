package fmrsabino.moviesdb.ui.search

import android.content.Context
import fmrsabino.moviesdb.data.DataManager
import fmrsabino.moviesdb.data.model.search.Search
import fmrsabino.moviesdb.ui.base.BasePresenter
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import timber.log.Timber
import javax.inject.Inject

class SearchPresenter(context: Context) : BasePresenter<SearchMvpView>(context) {
    private var view: SearchMvpView? = null
    @Inject lateinit var dataManager: DataManager

    private var searchResults: Search? = null
    private var previousQuery: String? = null

    private val subscriptions: CompositeSubscription = CompositeSubscription()

    init {
        inject()
    }

    public override fun onViewAttached(view: SearchMvpView) {
        this.view = view
    }

    public override fun onViewDetached() {
        this.view = null
        subscriptions.clear()
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
                    .subscribe({}, Throwable::printStackTrace)
        }
        subscriptions.add(observeDbImage())
    }

    private fun observeDbImage(): Subscription {
        return dataManager.observeImage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ image ->
                    val s = image.logoSizes?.filter { s -> s == "w185" }?.firstOrNull()
                    view?.getPosterImageUrl(image.baseUrl + s) },
                        { throwable -> Timber.e("Error loading image url") })
    }

    fun getSearch(query: String) {
        if (query == previousQuery) {
            Timber.i("Returning previous search")
            searchResults?.let { view?.showSearchResults(it, false) }
        } else {
            Timber.i("No search done. Fetching from web")
            subscriptions.add(performSearch(query))
        }
    }

    private fun performSearch(query: String): Subscription {
        return dataManager.getRemoteSearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { search ->
                            searchResults = search
                            previousQuery = query
                            view?.showSearchResults(search, true)
                        },
                        { throwable ->
                            Timber.e("Error performing search")
                            this.previousQuery = ""
                            val emptySearch = Search()
                            view?.showSearchResults(emptySearch, true)
                        })
    }

    fun setPreviousQuery(previousQuery: String) {
        this.previousQuery = previousQuery
    }
}