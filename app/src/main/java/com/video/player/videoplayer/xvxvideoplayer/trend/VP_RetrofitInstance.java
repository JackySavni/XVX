package com.video.player.videoplayer.xvxvideoplayer.trend;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VP_RetrofitInstance {
    VP_RetrofitInstance VPRetrofitInstance;
    VP_ApiInterface VPApiInterface;

    VP_RetrofitInstance(String baseURL) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VPApiInterface = retrofit.create(VP_ApiInterface.class);
    }

    public VP_RetrofitInstance getRetrofitInstance(String baseURL) {
        if (VPRetrofitInstance == null) {
            VPRetrofitInstance = new VP_RetrofitInstance(baseURL);
        }
        return VPRetrofitInstance;
    }
}
