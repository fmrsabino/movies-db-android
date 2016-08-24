package fmrsabino.moviesdb.data.model.configuration;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;

@AutoValue
public abstract class Image implements Parcelable {
    @Json(name = "base_url") public abstract String baseUrl();
    @Nullable @Json(name = "secure_base_url") public abstract String secureBaseUrl();
    @Nullable @Json(name = "backdrop_sizes") public abstract List<String> backdropSizes();
    @Nullable @Json(name = "poster_sizes") public abstract List<String> posterSizes();
    @Nullable @Json(name = "logo_sizes") public abstract List<String> logoSizes();

    public static JsonAdapter<Image> typeAdapter(Moshi moshi) {
        return new AutoValue_Image.MoshiJsonAdapter(moshi);
    }

    public static Builder builder() {
        return new AutoValue_Image.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder baseUrl(String baseUrl);
        public abstract Builder secureBaseUrl(String secureBaseUrl);
        public abstract Builder backdropSizes(List<String> backdropSizes);
        public abstract Builder posterSizes(List<String> posterSizes);
        public abstract Builder logoSizes(List<String> logoSizes);
        public abstract Image build();
    }
}
