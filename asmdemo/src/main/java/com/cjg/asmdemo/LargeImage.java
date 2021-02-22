package com.cjg.asmdemo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * 作    者：ZhouZhengyi
 * 创建日期：2020/4/4 20:58
 * 描    述：门面类，主要用于初始化和配置各项参数
 * 修订历史：2020/4/13 v1.0.0
 * github:https://github.com/121880399/LargeImageMonitor
 *
 *
 * https://blog.csdn.net/qq_42795723/article/details/107822387
 * ================================================
 */
public class LargeImage {
    public static String TAG = "LargeImageMonitor";

    public static Application APPLICATION;

    /**
     * 文件大小阈值 kb为单位
     */
    private double fileSizeThreshold = 500.0;
    /**
     * 内存大小阈值 kb为单位
     */
    private double memorySizeThreshold = 800.0;

    /**
     * 大图监控开关,这里只是存储数据的开关
     * 是否进行字节码修改需要在gradle文件中对插件进行设置
     */
    private volatile boolean largeImageOpen = true;


    /**
     * 用来记录当前启动次数
     */
    private MMKV mmkv;

    /**
     * 最大删除值，表示启动多少次以后开始清理mmkv记录的大图信息
     */
    private int maxRemoveValue = 20;

    /**
     * 是否已经调用了install方法
     */
    private boolean isCalled = false;

    /**
     * 当前删除值
     */
    private int currentRemoveValue = 1;

    private LargeImage() {

    }

    private static class Holder {
        private static LargeImage INSTANCE = new LargeImage();
    }

    public static LargeImage getInstance() {
        return Holder.INSTANCE;
    }


    /**
     * 是否清理mmkv中的值
     * 作者: ZhouZhengyi
     * 创建时间: 2020/4/11 21:20
     */
    private void isRemoveMmkv() {
        mmkv = MMKV.mmkvWithID("LargeImage");
        if (!mmkv.containsKey("maxRemoveValue")) {
            mmkv.encode("maxRemoveValue", maxRemoveValue);
        } else {
            maxRemoveValue = mmkv.decodeInt("maxRemoveValue");
        }
        if (!mmkv.containsKey("currentRemoveValue")) {
            mmkv.encode("currentRemoveValue", 1);
        } else {
            currentRemoveValue = mmkv.decodeInt("currentRemoveValue");
        }
        //开启一个线程，给保存大图信息的mmkv中的每个entry的unUseCount加1
        // 当前启动次数已经达到了该清理mmkv的最大值，那么开启一个线程清理存放大图信息的mmkv
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                MMKV largeImageMmkv = LargeImageManager.getInstance().getMmkv();
                for (String key : largeImageMmkv.allKeys()) {
                    LargeImageInfo largeImageInfo = largeImageMmkv.decodeParcelable(key, LargeImageInfo.class);
                    //所有保存的信息未使用次数加一
                    largeImageInfo.getUnUseCount().incrementAndGet();
                    largeImageMmkv.encode(key, largeImageInfo);
                }
                if (currentRemoveValue >= maxRemoveValue) {
                    for (String key : largeImageMmkv.allKeys()) {
                        LargeImageInfo largeImageInfo = largeImageMmkv.decodeParcelable(key, LargeImageInfo.class);
                        //如果该大图信息未使用次数大于最大删除值，那么说明该信息已经很久不用了，直接删除
                        if (largeImageInfo.getUnUseCount().get() >= maxRemoveValue) {
                            largeImageMmkv.remove(key);
                        }
                    }
                    //清理完后，当前启动次数清零
                    currentRemoveValue = 0;
                }
                currentRemoveValue++;
                mmkv.encode("currentRemoveValue", currentRemoveValue);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    /**
     * 设置文件阈值 单位为kb
     *
     * @param fileSizeThreshold 文件阈值 单位kb
     * @return
     */
    public LargeImage setFileSizeThreshold(double fileSizeThreshold) {
        this.fileSizeThreshold = fileSizeThreshold;
        return this;
    }

    /**
     * 设置内存阈值 单位为kb
     *
     * @param memorySizeThreshold 内存阈值 单位为kb
     * @return
     */
    public LargeImage setMemorySizeThreshold(double memorySizeThreshold) {
        this.memorySizeThreshold = memorySizeThreshold;
        return this;
    }

    /**
     * 设置大图监控开关 true为开 false为关
     */
    public LargeImage setLargeImageOpen(boolean largeImageOpen) {
        this.largeImageOpen = largeImageOpen;
        return this;
    }


    /**
     * 设置启动多少次，开始清除mmkv中的大图信息
     * 项目越大，该值可以适当调大
     */
    public LargeImage setMaxRemoveValue(int value) {
        //不建议少于20次
        if(value <= 20){
            value = 20;
        }
        //这里不设置this.maxRemoveValue的值是因为不想影响当前
        //的值，因为是否进行清理是在子线程中执行，只存放mmkv
        //那么只影响下次启动
        mmkv.encode("maxRemoveValue",value);
        return this;
    }


    public double getFileSizeThreshold() {
        return fileSizeThreshold;
    }

    public double getMemorySizeThreshold() {
        return memorySizeThreshold;
    }

    public boolean isLargeImageOpen() {
        return largeImageOpen;
    }

    public int getMaxRemoveValue() {
        return maxRemoveValue;
    }
}
