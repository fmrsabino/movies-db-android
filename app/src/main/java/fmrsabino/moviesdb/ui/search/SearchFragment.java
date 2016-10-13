package fmrsabino.moviesdb.ui.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import fmrsabino.moviesdb.R;
import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.ui.base.BaseFragment;
import fmrsabino.moviesdb.ui.base.PresenterLoader;
import fmrsabino.moviesdb.util.RxUtil;
import rx.Subscription;

public class SearchFragment extends BaseFragment implements SearchMvpView, LoaderManager.LoaderCallbacks<SearchPresenter> {
    private SearchPresenter presenter;
    private SearchAdapter adapter;

    @BindView(R.id.activity_search_list) RecyclerView recyclerView;
    @BindView(R.id.activity_search_query_field) EditText searchField;
    @BindView(R.id.activity_search_button) Button button;

    private Subscription buttonOnClick;

    //Loader Callbacks
    @Override
    public Loader<SearchPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(getContext(), () -> new SearchPresenter(getContext()));
    }

    @Override
    public void onLoadFinished(Loader<SearchPresenter> loader, SearchPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<SearchPresenter> loader) {}


    //Lifecycle


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_search, container, false);
        ButterKnife.bind(this, v);

        adapter = new SearchAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getSearch(searchField.getText().toString());
        buttonOnClick = RxView.clicks(button).subscribe(aVoid ->
                presenter.getSearch(searchField.getText().toString()));
    }

    @Override
    public void onPause() {
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
