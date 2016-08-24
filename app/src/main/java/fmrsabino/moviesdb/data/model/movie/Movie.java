package fmrsabino.moviesdb.data.model.movie;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.joda.time.DateTime;

@AutoValue
public abstract class Movie {
    @Nullable @Json(name = "backdrop_path") public abstract String backdropPath();
    @Nullable @Json(name = "poster_path") public abstract String posterPath();
    @Nullable @Json(name = "release_date") public abstract DateTime releaseDate();
    @Nullable @Json(name = "imdb_id") public abstract String imdbId();

    public abstract int id();
    @Nullable public abstract String title();
    @Nullable public abstract String tagline();
    @Nullable public abstract String overview();

    public static JsonAdapter<Movie> typeAdapter(Moshi moshi) {
        return new AutoValue_Movie.MoshiJsonAdapter(moshi);
    }

    public static Builder builder() {
        return new AutoValue_Movie.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder backdropPath(String backdropPath);
        public abstract Builder posterPath(String posterPath);
        public abstract Builder releaseDate(DateTime releaseDate);
        public abstract Builder imdbId(String imdbId);
        public abstract Builder id(int id);
        public abstract Builder title(String title);
        public abstract Builder tagline(String tagline);
        public abstract Builder overview(String overview);
        public abstract Movie build();
    }
}
