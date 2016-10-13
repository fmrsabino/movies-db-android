package fmrsabino.moviesdb.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fmrsabino.moviesdb.R;
import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.data.model.movie.Movie;
import fmrsabino.moviesdb.ui.base.BaseActivity;
import fmrsabino.moviesdb.ui.base.PresenterLoader;
import fmrsabino.moviesdb.util.RxUtil;
import rx.Subscription;

public class DetailActivity extends BaseActivity implements DetailMvpView, LoaderManager.LoaderCallbacks<DetailPresenter> {
    public static final String MOVIE_ID_EXTRA = "movieId";

    @Inject DataManager dataManager;
    @Inject DetailPresenter presenter;

    @BindView(R.id.activity_detail_movie_title) TextView movieTitle;
    @BindView(R.id.activity_detail_movie_cover) ImageView coverImage;
    @BindView(R.id.activity_detail_add_watchlist) Button watchlistButton;

    private int id;
    private boolean stored;

    private Subscription watchlistButtonClick;

    @Override
    public Loader<DetailPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, () -> new DetailPresenter(this));
    }

    @Override
    public void onLoadFinished(Loader<DetailPresenter> loader, DetailPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<DetailPresenter> loader) {}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportLoaderManager().initLoader(0, null, this);
        id = getIntent().getIntExtra(MOVIE_ID_EXTRA, -1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        watchlistButtonClick = RxView.clicks(watchlistButton)
                .subscribe(aVoid -> {
                    Movie movie = presenter.getActiveMovie();
                    if (stored) {
                        presenter.deleteMovie(Integer.toString(movie.id()));
                    } else {
                        presenter.saveMovie(movie);
                    }}, Throwable::printStackTrace);
    }

    @Override
    protected void onPause() {
        super.onPause();
        RxUtil.unsubscribe(watchlistButtonClick);
    }

    @Override
    protected void onViewAttached() {
        presenter.onViewAttached(this);
        presenter.loadImageUrl();
        presenter.getMovie(id);
    }

    @Override
    protected void onViewDetached() {
        presenter.onViewDetached();
    }

    @Override
    public void getMovieDetails(Movie movie) {
        movieTitle.setText(movie.title());
        reloadImages();
    }

    @Override
    public void getCoverUrl(String url) {
        reloadImages();
    }

    @Override
    public void getPosterUrl(String url) {
        reloadImages();
    }

    private void reloadImages() {
        Movie movie = presenter.getActiveMovie();
        if (movie != null) {
            Picasso.with(this).load(presenter.getPosterUrl() + movie.backdropPath())
                    .fit()
                    .centerCrop()
                    .into(coverImage);
        }
    }

    @Override
    public void savedMovie(Movie storedMovie) {
        stored = storedMovie != null;
        watchlistButton.setText(getString(stored ? R.string.watchlist_remove : R.string.watchlist_add));
    }
}
