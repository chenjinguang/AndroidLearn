package com.cjg.androidlearn;

import android.graphics.Bitmap;

import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.LinkedBlockingDeque;

public class RequestManager {

    private static RequestManager instance;

    private RequestManager(){
        int count = 5;

        for (int i = 0; i < count; i++) {
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(bitmapRequestQueue);
            bitmapDispatcher.start();
        }

    }

    public static RequestManager getInstance(){
        if(instance == null){
            synchronized (RequestManager.class){
                if(instance == null){
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    private LinkedBlockingDeque<BitmapRequest> bitmapRequestQueue = new LinkedBlockingDeque<>();

    public void addBitmapRequest(BitmapRequest bitmapRequest){
        bitmapRequestQueue.add(bitmapRequest);
    }



}
