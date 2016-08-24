package fmrsabino.moviesdb.data.model.search;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import org.joda.time.DateTime;

@AutoValue
public abstract class Result implements Parcelable {
    public abstract String title();
    public abstract int id();
    @Nullable public abstract String overview();
    @Nullable @Json(name = "backdrop_path") public abstract String backdropPath();
    @Nullable @Json(name = "poster_path") public abstract String posterPath();
    @Nullable @Json(name = "release_date") public abstract DateTime releaseDate();

    public static JsonAdapter<Result> typeAdapter(Moshi moshi) {
        return new AutoValue_Result.MoshiJsonAdapter(moshi);
    }

    public static Builder builder() {
        return new AutoValue_Result.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder title(String title);
        public abstract Builder id(int id);
        public abstract Builder overview(String overview);
        public abstract Builder backdropPath(String backdropPath);
        public abstract Builder posterPath(String posterPath);
        public abstract Builder releaseDate(DateTime releaseDate);
        public abstract Result build();
    }
}
