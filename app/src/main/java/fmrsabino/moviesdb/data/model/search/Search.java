package fmrsabino.moviesdb.data.model.search;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.util.List;

@AutoValue
public abstract class Search implements Parcelable {
    public abstract int page();
    public abstract List<Result> results();

    public static JsonAdapter<Search> typeAdapter(Moshi moshi) {
        return new AutoValue_Search.MoshiJsonAdapter(moshi);
    }

    public static Builder builder() {
        return new AutoValue_Search.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder page(int page);
        public abstract Builder results(List<Result> results);
        public abstract Search build();
    }
}
