package fmrsabino.moviesdb.data.model.configuration;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;

@AutoValue
public abstract class Configuration implements Parcelable {
    public abstract Image images();

    public static JsonAdapter<Configuration> typeAdapter(Moshi moshi) {
        return new AutoValue_Configuration.MoshiJsonAdapter(moshi);
    }

    public static Builder builder() {
        return new AutoValue_Configuration.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder images(Image image);
        public abstract Configuration build();
    }
}
