package com.shunyank.shunyankvideoplayer;

import static android.view.View.GONE;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shunyank.svideoplayer.player.PlayerActivity;

public class VideoPlayer extends PlayerActivity {


    @Override
    public int getLayoutResourceId() {
        return  R.layout.activity_video_player;
    }

    //return a view for snackBar otherwise app will crash
    @Override
    public View getMainLayoutResourceId() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        setFileOpenerVisibility(GONE);
        String url = getIntent().getStringExtra("url");

        setLink(url);
        initializePlayer();
    }
    //"https://r2---sn-cnoa-w5pe.googlevideo.com/videoplayback?expire=1630318277&ei=ZVosYdK0BMr_juMPgbS6qAk&ip=117.199.166.55&id=o-ADE5k2nbhLyREiUDoF5PsUJAe5Yl861DBqdqBXSQFpw6&itag=22&source=youtube&requiressl=yes&mh=uC&mm=31%2C29&mn=sn-cnoa-w5pe%2Csn-cnoa-qxae&ms=au%2Crdu&mv=m&mvi=2&pl=20&initcwndbps=550000&vprv=1&mime=video%2Fmp4&cnr=14&ratebypass=yes&dur=631.722&lmt=1610013178639794&mt=1630296290&fvip=2&fexp=24001373%2C24007246&c=ANDROID&txp=6316222&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cvprv%2Cmime%2Ccnr%2Cratebypass%2Cdur%2Clmt&sig=AOq0QJ8wRgIhAINEc__nSRezLYiH0HGtsl7dxGTld5yv2uhTalXVtaFtAiEAht_jrQl1i4qoOXfKVx2zWODupMmeGAQh-E7_5IpxmG0%3D&lsparams=mh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Cinitcwndbps&lsig=AG3C_xAwRQIgYdOCADp_CfzJWwejXodARPv32HRdE_hsIVDX5uKRAI8CIQDqMrzwA1jIKMS8iBEg5pz8vKIZMohJAlPPTjMHTT-sQw%3D%3D"


}