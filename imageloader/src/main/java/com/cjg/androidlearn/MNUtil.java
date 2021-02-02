package com.cjg.androidlearn;

import android.content.Context;

public class MNUtil {

    public static BitmapRequest with(Context context){
        return new BitmapRequest(context);
    }
}
