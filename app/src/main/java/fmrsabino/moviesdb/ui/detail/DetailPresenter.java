package fmrsabino.moviesdb.ui.detail;

import com.annimon.stream.Stream;

import javax.inject.Inject;

import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.ui.base.Presenter;
import fmrsabino.moviesdb.util.RxUtil;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailPresenter implements Presenter<DetailMvpView> {

    private DetailMvpView view;
    private final DataManager dataManager;

    private Subscription imageSubscription;

    @Inject
    public DetailPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onViewAttached(DetailMvpView view) {
        this.view = view;
    }

    @Override
    public void onViewDetached() {
        view = null;
        RxUtil.unsubscribe(imageSubscription);
    }

    @Override
    public void onDestroyed() {}

    public void loadImageUrl() {
        RxUtil.unsubscribe(imageSubscription);
        imageSubscription = dataManager.observeImage()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        image -> {
                            if (view != null) {
                                String posterSize = Stream.of(image.logoSizes())
                                        .filter(s -> s.equals("w185"))
                                        .findFirst()
                                        .get();
                                view.getPosterUrl(image.baseUrl() + posterSize);
                                String coverSize = Stream.of(image.backdropSizes())
                                        .filter(s -> s.equals("w1280"))
                                        .findFirst()
                                        .get();
                                view.getCoverUrl(image.baseUrl() + coverSize);
                            }
                        },
                        Throwable::printStackTrace,
                        () -> {});
    }

    public void getMovie(final int id) {
        dataManager.getRemoteMovie(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        movie -> {
                            if (view != null) {
                                view.getMovieDetails(movie);
                            }
                        },
                        Throwable::printStackTrace,
                        () -> {}
                );
    }
}
