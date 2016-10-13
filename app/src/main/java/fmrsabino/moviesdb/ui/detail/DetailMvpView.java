package fmrsabino.moviesdb.ui.detail;

import fmrsabino.moviesdb.data.model.movie.Movie;
import fmrsabino.moviesdb.ui.base.MvpView;

public interface DetailMvpView extends MvpView {
    void getMovieDetails(Movie movie);
    void getCoverUrl(String url);
    void getPosterUrl(String url);
    void savedMovie(Movie movie);
}
