package com.example.inky.api;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface UploadApi {
    @Multipart
    @POST("/picture")
    Call<ResponseBody> postFileWithParams(@Part List<MultipartBody.Part> partLis);
}
