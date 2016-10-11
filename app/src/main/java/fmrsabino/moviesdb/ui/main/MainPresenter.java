package fmrsabino.moviesdb.ui.main;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.annimon.stream.Stream;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.ui.base.Presenter;
import fmrsabino.moviesdb.util.RxUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainPresenter implements Presenter<MainMvpView> {

    private MainMvpView view;
    private final DataManager dataManager;

    private Search searchResults;
    private String previousQuery;

    private Subscription imageSubscription;
    private Subscription searchSubscription;


    @Inject
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onViewAttached(MainMvpView view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        this.view = null;
        RxUtil.unsubscribe(imageSubscription);
        RxUtil.unsubscribe(searchSubscription);
    }

    @Override
    public void onDestroyed() {}

    public void loadPosterImageUrl() {
        RxUtil.unsubscribe(imageSubscription);
        if (!dataManager.hasImage()) {
            dataManager.syncImage()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(image -> {}, Throwable::printStackTrace);
        }
        imageSubscription = dataManager.observeImage()
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
            RxUtil.unsubscribe(searchSubscription);
            searchSubscription = dataManager.getRemoteSearch(s)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            search -> {
                                this.searchResults = search;
                                this.previousQuery = s;
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
    }

    public void setPreviousQuery(String previousQuery) {
        this.previousQuery = previousQuery;
    }
}
