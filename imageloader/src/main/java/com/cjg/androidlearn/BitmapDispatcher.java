package com.cjg.androidlearn;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.LinkedBlockingDeque;

public class BitmapDispatcher extends Thread {

    private Handler hander = new Handler();

    private LinkedBlockingDeque<BitmapRequest> queue;

    public BitmapDispatcher(LinkedBlockingDeque<BitmapRequest> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()){
                //从队列中获取数据
                BitmapRequest bitmapRequest = queue.take();
                //先展示占位图片
                showPlaceHolderImage(bitmapRequest);
                //然后从网络加载
                Bitmap bitmap = loadBitmapFromUrl(bitmapRequest);
                //然后将图片显示到ImageView上
                showBitmapOnUI(bitmapRequest,bitmap);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void showBitmapOnUI(BitmapRequest bitmapRequest, Bitmap bitmap) {
        if (bitmap != null && bitmapRequest != null &&
          bitmapRequest.getUrlMd5().equals(bitmapRequest.getImageView().getTag())){
            hander.post(new Runnable() {
                @Override
                public void run() {
                    bitmapRequest.getImageView().setImageBitmap(bitmap);
                }
            });
        }
    }

    private void showPlaceHolderImage(BitmapRequest bitmapRequest) {
        int loadingResId = bitmapRequest.getLoadingResId();
        if(loadingResId > 0){
            bitmapRequest.getImageView().setImageResource(loadingResId);
        }

    }

    public Bitmap loadBitmapFromUrl(BitmapRequest request){
        InputStream inputStream = null;
        try {
            URL uurl = new URL(request.getImageUrl());
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
}
