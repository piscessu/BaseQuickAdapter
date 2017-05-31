package com.pisces.basequickadapter;

import com.pisces.basequickadapter.example.MovieEntity;

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
        /**
         * @param start 起始index
         * @param count 每页多少条数据
         * @return 返回查询结果列表
         */
        @GET("top250")
        Call<MovieEntity> getTopMovie(@Query("start") int start, @Query("count") int count);
    }
}
