package fmrsabino.moviesdb.ui.main;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fmrsabino.moviesdb.R;
import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.ui.base.BaseActivity;
import fmrsabino.moviesdb.ui.base.PresenterLoader;
import fmrsabino.moviesdb.util.RxUtil;
import rx.Subscription;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, LoaderManager.LoaderCallbacks<MainPresenter> {
    @Inject DataManager dataManager;
    @Inject MainPresenter presenter;
    private SearchAdapter adapter;

    @BindView(R.id.activity_main_list) RecyclerView recyclerView;
    @BindView(R.id.activity_main_search_text) EditText searchField;
    @BindView(R.id.activity_main_button) Button button;

    private Subscription buttonOnClick;

    //Loader Callbacks
    @Override
    public Loader<MainPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, () -> new MainPresenter(dataManager));
    }

    @Override
    public void onLoadFinished(Loader<MainPresenter> loader, MainPresenter presenter) {
        Timber.d(presenter.toString());
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<MainPresenter> loader) {}


    //Lifecycle
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportLoaderManager().initLoader(0, null, this);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        this.adapter = new SearchAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getSearch(searchField.getText().toString());
        buttonOnClick = RxView.clicks(button).subscribe(aVoid ->
                presenter.getSearch(searchField.getText().toString()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        RxUtil.unsubscribe(buttonOnClick);
    }

    @Override
    protected void onViewAttached() {
        presenter.onViewAttached(this);
        presenter.loadPosterImageUrl();
    }

    @Override
    protected void onViewDetached() {
        presenter.onViewDetached();
    }

    @Override
    protected void inject() {
        getActivityComponent().inject(this);
    }

    //View
    @Override
    public void showSearchResults(Search search, boolean newResults) {
        if (newResults) {
            recyclerView.scrollToPosition(0);
        }
        adapter.setResults(search.results());
    }

    @Override
    public void getPosterImageUrl(String posterPath) {
        if (adapter != null) {
            adapter.setPosterBaseUrl(posterPath);
        }
    }
}
