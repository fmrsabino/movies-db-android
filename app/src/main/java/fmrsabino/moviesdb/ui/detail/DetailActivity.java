package fmrsabino.moviesdb.ui.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import fmrsabino.moviesdb.R;
import fmrsabino.moviesdb.data.DataManager;
import fmrsabino.moviesdb.data.model.movie.Movie;
import fmrsabino.moviesdb.ui.base.BaseActivity;
import fmrsabino.moviesdb.ui.base.PresenterLoader;

public class DetailActivity extends BaseActivity implements DetailMvpView, LoaderManager.LoaderCallbacks<DetailPresenter> {

    @Inject DataManager dataManager;
    @Inject DetailPresenter presenter;

    @BindView(R.id.activity_detail_movie_title) TextView movieTitle;
    @BindView(R.id.activity_detail_movie_cover) ImageView coverImage;

    private int id;
    private Movie movie;
    private String coverUrl;
    private String posterUrl;

    @Override
    public Loader<DetailPresenter> onCreateLoader(int id, Bundle args) {
        return new PresenterLoader<>(this, () -> new DetailPresenter(dataManager));
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
        id = getIntent().getIntExtra("movieId", -1);
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
    protected void inject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void getMovieDetails(Movie movie) {
        this.movie = movie;
        movieTitle.setText(movie.title());
        reloadImages();
    }

    @Override
    public void getCoverUrl(String url) {
        coverUrl = url;
        reloadImages();
    }

    @Override
    public void getPosterUrl(String url) {
        posterUrl = url;
        reloadImages();
    }

    private void reloadImages() {
        if (movie != null) {
            Picasso.with(this).load(coverUrl + movie.backdropPath())
                    .fit()
                    .centerCrop()
                    .into(coverImage);
        }
    }
}
