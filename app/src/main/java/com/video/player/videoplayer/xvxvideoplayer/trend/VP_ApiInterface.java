package com.video.player.videoplayer.xvxvideoplayer.trend;

import com.google.gson.JsonElement;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface VP_ApiInterface {
    @GET("{id}")
    Call<JsonElement> getCats(@Path("id") String id);
    @GET("{id}")
    Call<JsonElement> getVidList(@Path("id") String id);
    @GET("{id}")
    Call<JsonElement> getGIFList(@Path("id") String id);

}


