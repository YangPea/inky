package com.example.inky.api;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ImageApi {

    @GET("/pictures")
    Call<ResponseBody> getJson();

}