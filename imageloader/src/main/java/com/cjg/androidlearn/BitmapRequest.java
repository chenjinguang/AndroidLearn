package com.cjg.androidlearn;

import android.content.Context;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

public class BitmapRequest {

    private SoftReference<ImageView> imageView;

    private String imageUrl;

    private String urlMd5;

    private Context context;

    private int loadingResId;

    public BitmapRequest load(String url){
        this.imageUrl = url;
        this.urlMd5 = MD5Utils.toMd5(url);
        return this;
    }

    public BitmapRequest placeHolder(int placeHolder){
        this.loadingResId = placeHolder;
        return this;
    }

    public BitmapRequest(Context context) {
        this.context = context;
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public void setImageView(ImageView imageView) {
        this.imageView = new SoftReference<>(imageView);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public void setUrlMd5(String urlMd5) {
        this.urlMd5 = urlMd5;
    }

    public int getLoadingResId() {
        return loadingResId;
    }

    public void into(ImageView imageView) {
        this.imageView = new SoftReference<>(imageView);
        this.imageView.get().setTag(urlMd5);
        RequestManager.getInstance().addBitmapRequest(this);
    }
}
