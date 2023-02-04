package com.shunyank.shunyankvideoplayer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shunyank.shunyankvideoplayer.callbacks.ItemClickListener;
import com.shunyank.shunyankvideoplayer.network.Api;
import com.shunyank.shunyankvideoplayer.network.RetrofitHelper;
import com.shunyank.shunyankvideoplayer.network.VideoModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CompletedVideosActivity extends AppCompatActivity {

    RecyclerView list;
    String[] listItem;
    private ArrayAdapter<String> mHistory;
    VideosListAdapter adapter;
    SharedPreferences sharedPreferences;
    ArrayList<VideoModel> videoModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_videos);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPreferences = getSharedPreferences("VIDEO_PREF",MODE_PRIVATE);
        videoModelArrayList = new ArrayList<>();
        adapter = new VideosListAdapter();
        adapter.setAdapterForCompleted(true);
        adapter.setData(videoModelArrayList);

        ImageButton back = findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompletedVideosActivity.super.onBackPressed();
            }
        });

        adapter.setListener(new ItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                VideoModel value = (VideoModel) item;
                Intent player = new Intent(getBaseContext(),VideoPlayer.class);
                player.putExtra("url",value.getUrl());
                startActivity(player);
            }

            @Override
            public void onMarkAsComplete(Object item, int pos) {


            }

            @Override
            public void onRemoveAsComplete(Object item, int pos) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(CompletedVideosActivity.this  );
                dialog.setMessage("Do you want to remove "+((VideoModel)item).getName()+ " as completed?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeVideoAsCompleted((VideoModel) item);
                        setDataToAdapter();

                    }
                });

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        list = findViewById(R.id.listview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(layoutManager);
        list.setAdapter(adapter);
        setDataToAdapter();
//
//        Retrofit retrofit = new RetrofitHelper(this).getRetrofit();
//        Api api = retrofit.create(Api.class);
//        Call<List<String>> call = api.getMovies();
//        call.enqueue(new Callback<List<String>>() {
//            @Override
//            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
//                List<String> videos = response.body();
//
//                for (String url:videos){
//                   String fileName  =  new File(Uri.parse(url).getPath()).getName();
//                    videoModelArrayList.add(new VideoModel(fileName,url));
//                }
//
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<List<String>> call, Throwable t) {
//                t.printStackTrace();
//            }
//        });




    }
    public void removeVideoAsCompleted(VideoModel videoModel){
        ArrayList<VideoModel> list = new Gson().fromJson(sharedPreferences.getString("completed_videos",""), new TypeToken<ArrayList<VideoModel>>(){}.getType());

        if(list!=null) {
            for (int i = 0; i < list.size(); i++) {

                if (list.get(i).getUrl().contentEquals(videoModel.getUrl())) {
                    list.remove(i);
                    break;
                }

            }
            sharedPreferences.edit().putString("completed_videos", new Gson().toJson(list)).apply();
        }

    }

    public void setDataToAdapter(){
        ArrayList<VideoModel> completed_videosList = new Gson().fromJson(sharedPreferences.getString("completed_videos",""), new TypeToken<ArrayList<VideoModel>>(){}.getType());


        if(completed_videosList==null) {
            completed_videosList = new ArrayList<>();


        }

        Collections.sort(completed_videosList, new Comparator<VideoModel>() {

            @Override
            public int compare(VideoModel o1, VideoModel o2) {
                return o1.getName().compareTo(o2.getName());
            }

            @Override
            public boolean equals(Object obj) {
                return false;
            }
        });
        adapter.setData(completed_videosList);
        adapter.notifyDataSetChanged();
    }
}