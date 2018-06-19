package com.example.lib.myapplication;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {
    private MyVideoView vv_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //视频文件列表
        ArrayList<String> videoFileLists = new ArrayList<String>();
        //u盘位置
        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //U盘上的文件夹
        String videoDir = "tvshow";
        //目录下的文件列表
        File files[] = null;

        //目录不存在，则创建目录
        File dir = new File(filePath + "/" + videoDir);
        if (!dir.exists()) {
            dir.mkdirs();
            Toast.makeText(getApplicationContext(), "请在U盘的" + videoDir + "文件夹中放一个*.mp4文件!", Toast.LENGTH_LONG).show();
        }

        //查找目录下的所有文件
        do {
            files = dir.listFiles();
            if (files == null) {
                Toast toast = Toast.makeText(getApplicationContext(), "空目录，请在U盘的" + videoDir + "文件夹中放一个*.mp4文件！", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                //延时等待
                try {
                    Thread.sleep(3500);
                    continue;
                } catch (InterruptedException e) {
                }
            }
            //将文件压入videoFileLists
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    continue;
                } else {
                    if (files[i].getName().toLowerCase().endsWith("mp4")) {
                        videoFileLists.add(files[i].getAbsolutePath());
                    }
                }
            }

            //没找到文件
            if (videoFileLists.isEmpty()) {
                Toast toast = Toast.makeText(getApplicationContext(), "没找到文件,请在U盘的" + videoDir + "文件夹中放一个*.mp4文件！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                //延时等待
                try {
                    Thread.sleep(3500);
                    continue;
                } catch (InterruptedException e) {
                }
            } else {
                break;
            }
        } while (true);

        //提取第一个文件名
        File file = new File(videoFileLists.get(0));
        if (file.exists()) {
            vv_video = (MyVideoView) findViewById(R.id.videoView);
            vv_video.setMediaController(new MediaController(this));
            vv_video.setVideoPath(file.getAbsolutePath());
            try {
                vv_video.start();
            }
            catch(Exception e)
            {
                Toast toast = Toast.makeText(getApplicationContext(), videoFileLists.get(0)+"，文件格式系统不支持！", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }

            vv_video.requestFocus();
            //循环播放
            vv_video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mPlayer) {
                    //循环播放
                    mPlayer.start();
                    mPlayer.setLooping(true);
                }
            });
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), videoFileLists.get(0) + "没找到，请在U盘的" + videoDir + "文件夹中放一个*.mp4文件！", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }
}
