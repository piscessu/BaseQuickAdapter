package com.pisces.basequickadapter;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PiscesSu on 2017/4/21.
 * Version 1.0
 */

public class RetrofitManager {

    private static Retrofit sRetrofit;
    private static final String BASE_URL = "https://api.douban.com/v2/movie/";

    public static RetrofitService getService() {
        if (sRetrofit == null) {
            synchronized (RetrofitManager.class) {
                if (sRetrofit == null) {
                    sRetrofit = new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return sRetrofit.create(RetrofitService.class);
    }

    interface RetrofitService {
        @GET("top250")
        Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
    }
}
