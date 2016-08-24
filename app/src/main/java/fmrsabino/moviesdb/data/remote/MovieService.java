package fmrsabino.moviesdb.data.remote;

import fmrsabino.moviesdb.data.model.configuration.Configuration;
import fmrsabino.moviesdb.data.model.movie.Movie;
import fmrsabino.moviesdb.data.model.search.Search;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface MovieService {
    @GET("configuration")
    Observable<Configuration> getConfiguration();

    @GET("search/movie")
    Observable<Search> getSearch(@Query("query") String query);

    @GET("movie/{id}")
    Observable<Movie> getMovie(@Path("id") int id);
}
