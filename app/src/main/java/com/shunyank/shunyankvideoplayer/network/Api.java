package com.shunyank.shunyankvideoplayer.network;

import android.text.Editable;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    // API PREFIX FOR OFFLINE
    String BASE_URL = "http://sampatsharma.com/";

    // Api call for fetching Top Courses
    @GET("uploads.php")
    Call<List<String>> getMovies();



}

