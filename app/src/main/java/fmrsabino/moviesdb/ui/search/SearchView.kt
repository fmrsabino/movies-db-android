package fmrsabino.moviesdb.ui.search

import android.content.Context
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.jakewharton.rxbinding2.view.clicks
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.data.model.search.Search
import fmrsabino.moviesdb.ui.base.PresenterLoader
import kotlinx.android.synthetic.main.activity_search.view.*
import rx.Subscription
import timber.log.Timber

class SearchView(context: Context) : LinearLayout(context), SearchMvpView, LoaderManager.LoaderCallbacks<SearchPresenter> {
    private var presenter: SearchPresenter? = null
    private val adapter: SearchAdapter = SearchAdapter(context)

    private var buttonOnClick: Subscription? = null

    init {
        if (context is AppCompatActivity) {
            //hack
            context.supportLoaderManager.initLoader(0, Bundle(), this)
        }
        LayoutInflater.from(context).inflate(R.layout.activity_search, this, true)
    }

    //Loader Callbacks
    override fun onCreateLoader(id: Int, args: Bundle): Loader<SearchPresenter> {
        return PresenterLoader(context, { SearchPresenter(context) })
    }

    override fun onLoadFinished(loader: Loader<SearchPresenter>, presenter: SearchPresenter) {
        this.presenter = presenter
        Timber.i(presenter.toString())
    }

    override fun onLoaderReset(loader: Loader<SearchPresenter>) {}

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter?.onViewAttached(this)
        activity_search_list.layoutManager = LinearLayoutManager(context)
        activity_search_list.adapter = adapter
        presenter?.loadPosterImageUrl()
        presenter?.getSearch(activity_search_query_field.text.toString())
        activity_search_button.clicks().subscribe { presenter?.getSearch(activity_search_query_field.text.toString()) }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter?.onViewDetached()
        buttonOnClick?.unsubscribe()
    }

    //View
    override fun showSearchResults(search: Search, newResults: Boolean) {
        if (newResults) {
            activity_search_list.scrollToPosition(0)
        }
        adapter.setResults(search.results)
    }

    override fun getPosterImageUrl(posterPath: String) {
        adapter.baseUrl = posterPath
    }
}
