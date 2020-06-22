package com.example.inky.api;

import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.http.GET;

public interface ResourceApi {

    @GET("/resource.json")
    Call<ResponseBody> getJson();
}
