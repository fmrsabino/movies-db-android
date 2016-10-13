package fmrsabino.moviesdb.ui.detail;

import android.content.Context;

import com.annimon.stream.Stream;

import javax.inject.Inject;

import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.data.model.movie.Movie;
import fmrsabino.moviesdb.ui.base.BasePresenter;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public final class DetailPresenter extends BasePresenter<DetailMvpView> {
    private DetailMvpView view;
    @Inject DataManager dataManager;

    private final CompositeSubscription subscriptions = new CompositeSubscription();

    private Movie activeMovie;
    private String posterUrl;
    private String coverUrl;

    DetailPresenter(Context context) {
        super(context);
    }

    @Override
    public void onViewAttached(DetailMvpView view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        view = null;
        subscriptions.clear();
    }

    @Override
    public void onDestroyed() {}

    @Override
    protected void inject() {
        presenterComponent.inject(this);
    }

    void loadImageUrl() {
        if (!dataManager.hasImage()) {
            subscriptions.add(syncRemoteImage());
        }
        subscriptions.add(observeDbImage());
    }

    private Subscription syncRemoteImage() {
        return dataManager.syncImage()
                .subscribeOn(Schedulers.io())
                .subscribe(image -> {}, Throwable::printStackTrace);
    }

    private Subscription observeDbImage() {
        return dataManager.observeImage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        image -> {
                            if (view != null) {
                                String posterSize = Stream.of(image.logoSizes())
                                        .filter(s -> s.equals("w185"))
                                        .findFirst()
                                        .get();
                                posterUrl = image.baseUrl() + posterSize;
                                view.getPosterUrl(posterUrl);
                                String coverSize = Stream.of(image.backdropSizes())
                                        .filter(s -> s.equals("w1280"))
                                        .findFirst()
                                        .get();
                                coverUrl = image.baseUrl() + coverSize;
                                view.getCoverUrl(coverUrl);
                            }
                        },
                        Throwable::printStackTrace,
                        () -> {});
    }

    void getMovie(final int id) {
        String movieId = Integer.toString(id);
        if (activeMovie != null) {
            view.getMovieDetails(activeMovie);
        } else if (!dataManager.hasMovie(movieId)) {
            subscriptions.add(getRemoteMovie(id));
        }
        subscriptions.add(observeDbMovie(movieId));
    }

    private Subscription getRemoteMovie(final int id) {
        return dataManager.getRemoteMovie(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movie -> {
                            activeMovie = movie;
                            if (view != null) view.getMovieDetails(activeMovie);
                        },
                        Throwable::printStackTrace,
                        () -> {}
                );
    }

    private Subscription observeDbMovie(final String movieId) {
        return dataManager.observeMovie(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        movies -> {
                            if (view == null) return;
                            if (!movies.isEmpty()) {
                                if (activeMovie == null) {
                                    activeMovie = movies.get(0);
                                }
                                view.savedMovie(activeMovie);
                            } else {
                                view.savedMovie(null);
                            }
                        },
                        Throwable::printStackTrace);
    }

    void saveMovie(final Movie movie) {
        dataManager.storeMovie(movie)
                .subscribe(
                        m -> {},
                        Throwable::printStackTrace);
    }

    void deleteMovie(final String movieId) {
        dataManager.deleteMovie(movieId)
                .subscribe(
                        s -> {},
                        Throwable::printStackTrace);
    }

    Movie getActiveMovie() {
        return activeMovie;
    }

    String getPosterUrl() {
        return posterUrl;
    }

    String getCoverUrl() {
        return coverUrl;
    }
}
