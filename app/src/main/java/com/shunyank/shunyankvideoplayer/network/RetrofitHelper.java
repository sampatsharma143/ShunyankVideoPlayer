package com.shunyank.shunyankvideoplayer.network;

import static com.shunyank.shunyankvideoplayer.network.Api.BASE_URL;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    Context context;

    public RetrofitHelper(Context context){
        this.context = context;
    }
    public Retrofit getRetrofit(){

        OkHttpClient okClient = new OkHttpClient.Builder().build();




        return new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(okClient)
                .build();
    }

}
