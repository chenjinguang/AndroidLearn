package com.cjg.asmdemo;

import android.app.Application;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2021/2/3
 * @修改人和其它信息
 */
public class App  extends Application {

    public static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
