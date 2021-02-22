package com.cjg.mmkvdemo;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.tencent.mmkv.MMKV;
import com.tencent.mmkv.MMKVHandler;
import com.tencent.mmkv.MMKVLogLevel;
import com.tencent.mmkv.MMKVRecoverStrategic;

import java.io.File;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2021/2/20
 * @修改人和其它信息
 */
public class MmkvApplication extends Application implements MMKVHandler {

    @Override
    public void onCreate() {
        super.onCreate();
        String filePath = getApplicationContext().getFilesDir().getAbsolutePath() + "/mmkv";
        File file = new File(filePath);
        if (!file.exists()){
            file.mkdir();
        }
        String rootDir = MMKV.initialize(filePath);
        System.out.println("mmkv root: " + rootDir);
    }

    @Override
    public MMKVRecoverStrategic onMMKVCRCCheckFail(String s) {
        return null;
    }

    @Override
    public MMKVRecoverStrategic onMMKVFileLengthError(String s) {
        return null;
    }

    @Override
    public boolean wantLogRedirecting() {
        return true;
    }

    @Override
    public void mmkvLog(MMKVLogLevel level, String file, int line, String func, String message) {
        String log = "<" + file + ":" + line + "::" + func + "> " + message;
        switch (level) {
            case LevelDebug:
                Log.d("redirect logging MMKV", log);
                break;
            case LevelInfo:
                //Log.i("redirect logging MMKV", log);
                break;
            case LevelWarning:
                //Log.w("redirect logging MMKV", log);
                break;
            case LevelError:
                //Log.e("redirect logging MMKV", log);
                break;
            case LevelNone:
                //Log.e("redirect logging MMKV", log);
                break;
        }
    }
}
