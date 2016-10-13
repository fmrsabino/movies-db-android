package fmrsabino.moviesdb.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.ui.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class SearchPresenter extends BasePresenter<SearchMvpView> {
    private SearchMvpView view;
    @Inject DataManager dataManager;

    private Search searchResults;
    private String previousQuery;

    private CompositeSubscription subscriptions = new CompositeSubscription();

    @Inject
    public SearchPresenter(Context context) {
        super(context);
    }

    @Override
    public void onViewAttached(SearchMvpView view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
        subscriptions.clear();
    }

    @Override
    public void onDestroyed() {}

    @Override
    protected void inject() {
        presenterComponent.inject(this);
    }

    public void loadPosterImageUrl() {
        if (!dataManager.hasImage()) {
            dataManager.syncImage()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(image -> {}, Throwable::printStackTrace);
        }
        subscriptions.add(observeDbImage());
    }

    private Subscription observeDbImage() {
        return dataManager.observeImage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        image -> {
                            if (view != null) {
                                List<String> logoSizes = image.logoSizes();
                                if (logoSizes == null) { return; }
                                String first = Stream.of(logoSizes)
                                        .filter(s -> s.equals("w185"))
                                        .findFirst()
                                        .get();
                                view.getPosterImageUrl(image.baseUrl() + first);
                            }
                        },
                        throwable -> Timber.e("Error loading image url"),
                        () -> {});
    }

    public void getSearch(@NonNull String s) {
        if (view == null || TextUtils.isEmpty(s)) { return; }
        if (TextUtils.isEmpty(s) || TextUtils.equals(s, previousQuery)) {
            Timber.i("Returning previous search");
            view.showSearchResults(searchResults, false);
        } else {
            Timber.i("No search done. Fetching from web");
            subscriptions.add(performSearch(s));
        }
    }

    private Subscription performSearch(final String query) {
        return dataManager.getRemoteSearch(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        search -> {
                            this.searchResults = search;
                            this.previousQuery = query;
                            view.showSearchResults(search, true);
                        },
                        throwable -> {
                            Timber.e("Error performing search");
                            this.previousQuery = "";
                            Search emptySearch = Search.builder().page(1).results(new ArrayList<>()).build();
                            view.showSearchResults(emptySearch, true);
                        },
                        () -> {}
                );
    }

    public void setPreviousQuery(String previousQuery) {
        this.previousQuery = previousQuery;
    }
}
