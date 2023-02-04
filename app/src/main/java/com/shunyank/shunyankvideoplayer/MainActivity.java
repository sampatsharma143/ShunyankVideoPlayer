package com.shunyank.shunyankvideoplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

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
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    RecyclerView list;
    String[] listItem;
    private ArrayAdapter<String> mHistory;
    VideosListAdapter adapter;
    SharedPreferences sharedPreferences;
    ArrayList<VideoModel> videoModelArrayList;
    ProgressBar progress_bar;
    private boolean paused=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        sharedPreferences = getSharedPreferences("VIDEO_PREF",MODE_PRIVATE);
        videoModelArrayList = new ArrayList<>();
        adapter = new VideosListAdapter();
        adapter.setData(videoModelArrayList);
        progress_bar = findViewById(R.id.progress_bar);
        findViewById(R.id.completed_videos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,CompletedVideosActivity.class));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this  );
                dialog.setMessage("Do you want to mark "+((VideoModel)item).getName()+ " as completed?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addVideoAsCompleted((VideoModel) item);
                        fetchData();
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

        fetchData();






    }

    public void fetchData(){

        progress_bar.setVisibility(View.VISIBLE);
        videoModelArrayList = new ArrayList<>();
        Retrofit retrofit = new RetrofitHelper(this).getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<List<String>> call = api.getMovies();
        call.enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                List<String> videos = response.body();
                progress_bar.setVisibility(View.GONE);
                for (String url:videos){
                    String fileName  =  new File(Uri.parse(url).getPath()).getName();
                    videoModelArrayList.add(new VideoModel(fileName,url));
                }
                ArrayList<VideoModel> list = new Gson().fromJson(sharedPreferences.getString("completed_videos",""), new TypeToken<ArrayList<VideoModel>>(){}.getType());

                Log.e("lis",new Gson().toJson(list));

                if(list!=null) {
                    for (int i=0;i<videoModelArrayList.size();i++){

                        for (int j=0;j<list.size();j++){

                            if(list.get(j).getUrl().contentEquals(videoModelArrayList.get(i).getUrl())){

                                videoModelArrayList.remove(i);

                            }

                        }

                    }
                }

                Collections.sort(videoModelArrayList, new Comparator<VideoModel>() {

                    @Override
                    public int compare(VideoModel o1, VideoModel o2) {
                        return o1.getName().compareTo(o2.getName());
                    }

                    @Override
                    public boolean equals(Object obj) {
                        return false;
                    }
                });
                adapter.setData(videoModelArrayList);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                t.printStackTrace();
                progress_bar.setVisibility(View.GONE);

            }
        });

    }
    public void addVideoAsCompleted(VideoModel videoModel){
        ArrayList<VideoModel> list = new Gson().fromJson(sharedPreferences.getString("completed_videos",""), new TypeToken<ArrayList<VideoModel>>(){}.getType());

        if(list==null){
            list = new ArrayList<VideoModel>();
        }

        list.add((VideoModel) videoModel);
        sharedPreferences.edit().putString("completed_videos", new Gson().toJson(list)).apply();

    }


    public static  <T> ArrayList<T>  listToArrayList(List<T> list) {
        ArrayList<T> arrayList = new ArrayList<>();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                arrayList.add(list.get(i));
                i++;
            }
            // or simply
            // arrayList.addAll(list);
        }
        return  arrayList;
    }

    @Override
    protected void onPause() {
        super.onPause();
        paused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(paused){
            fetchData();
            paused = false;
        }

    }
}