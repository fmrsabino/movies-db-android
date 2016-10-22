package fmrsabino.moviesdb.ui.main


import android.os.Bundle
import fmrsabino.moviesdb.R
import fmrsabino.moviesdb.ui.base.BaseActivity
import fmrsabino.moviesdb.ui.search.SearchView

class MainActivity : BaseActivity() {
    private var selectedTabId: Int = 0
    private var searchView: SearchView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchView = SearchView(this)
    }

    override fun onResume() {
        super.onResume()
        /*bottomBar?.setOnTabSelectListener { tabId ->
            //if (selectedTabId == tabId) return;
            selectedTabId = tabId
            container?.removeAllViews()
            when (tabId) {
                R.id.tab_search -> container?.addView(searchView)
                R.id.tab_favorites -> {}
            }
        }*/
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("tabId", selectedTabId)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        selectedTabId = savedInstanceState.getInt("tabId")
    }

    override fun onViewAttached() {}

    override fun onViewDetached() {}
}
