package fmrsabino.moviesdb.data.remote;


import com.squareup.moshi.Moshi;

import fmrsabino.moviesdb.util.AutoValueAdapterFactory;
import fmrsabino.moviesdb.util.Constants;
import fmrsabino.moviesdb.util.DateTypeAdapter;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitHelper {
    public static MovieService newMovieService() {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(getInterceptor()).build();

        Moshi moshi = new Moshi.Builder()
                .add(AutoValueAdapterFactory.create())
                .add(new DateTypeAdapter())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit.create(MovieService.class);
    }

    private static Interceptor getInterceptor() {
        return chain -> {
            Request request = chain.request();
            HttpUrl.Builder builder = request.url().newBuilder();
            HttpUrl url = builder.addQueryParameter("api_key", Constants.API_KEY).build();
            request = request.newBuilder().url(url).build();
            return chain.proceed(request);
        };
    }
}
