package com.cjg.androidlearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    Bitmap bitmap;

    String url = "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1603365312,3218205429&fm=26&gp=0.jpg";


    @SuppressLint("HandlerLeak")
    private static final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageview);


        LinkedHashMap<Integer, Integer> map = new LinkedHashMap<>(
                0, 0.75f, true);
        map.put(0, 0);
        map.put(1, 1);
        map.put(2, 2);
        map.put(3, 3);
        map.put(4, 4);
        map.put(5, 5);
        map.put(6, 6);
        map.get(1);
        map.get(2);

        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());

        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                bitmap = loadBitmapFromUrl(url);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("GGG",bitmap.getByteCount() + "");
//                        imageView.setImageBitmap(bitmap);
//                    }
//                });
//            }
//        }).start();

        MNUtil.with(this).placeHolder(R.mipmap.ic_launcher).load(url).into(imageView);
    }

    public Bitmap loadBitmapFromUrl(String url){
        InputStream inputStream = null;
        try {
            URL uurl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) uurl.openConnection();
            inputStream = connection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(inputStream != null){
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(this);
    }
}