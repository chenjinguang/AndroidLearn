package com.cjg.hermeseventbus;

import android.app.Application;

import xiaofei.library.hermeseventbus.HermesEventBus;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2021/2/20
 * @修改人和其它信息
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        HermesEventBus.getDefault().init(this);
    }
}
