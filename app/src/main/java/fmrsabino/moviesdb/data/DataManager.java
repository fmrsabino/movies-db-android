package fmrsabino.moviesdb.data;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import fmrsabino.moviesdb.data.local.DatabaseHelper;
import fmrsabino.moviesdb.data.model.configuration.Configuration;
import fmrsabino.moviesdb.data.model.configuration.Image;
import fmrsabino.moviesdb.data.model.movie.Movie;
import fmrsabino.moviesdb.data.model.search.Search;
import fmrsabino.moviesdb.data.remote.MovieService;
import rx.Observable;
import timber.log.Timber;

@Singleton
public class DataManager {
    private final MovieService movieService;
    private final DatabaseHelper databaseHelper;

    @Inject
    public DataManager(MovieService movieService, DatabaseHelper databaseHelper) {
        this.movieService = movieService;
        this.databaseHelper = databaseHelper;
    }

    public Observable<Configuration> getRemoteConfiguration() {
        return movieService.getConfiguration();
    }

    public Observable<Search> getRemoteSearch(@NonNull final String query) {
        return movieService.getSearch(query);
    }

    public Observable<Image> syncImage() {
        return movieService.getConfiguration()
                .concatMap(config -> databaseHelper.setImage(config.images()))
                .doOnNext(image -> Timber.i("Synced image - %s", image.toString()));
    }

    public Observable<Image> observeImage() {
        return databaseHelper.hasImage() ? databaseHelper.observeImage() : syncImage();
    }

    public Observable<Movie> syncMovie(final int id) {
        return movieService.getMovie(id)
                .concatMap(databaseHelper::setMovie)
                .doOnNext(movie -> Timber.i("Synced movie (%s)", movie.title()));
    }

    public Observable<Movie> getRemoteMovie(final int id) {
        return movieService.getMovie(id);
    }
}
