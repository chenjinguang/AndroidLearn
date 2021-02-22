package com.cjg.mmkvdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.tencent.mmkv.MMKV;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MMKV mmkv = MMKV.defaultMMKV();

        mmkv.encode("bool",true);

        boolean bool = mmkv.decodeBool("bool", false);

        System.out.println(bool);

        byte[] bytes = {'m','m','k','v'};
        mmkv.encode("bytes",bytes);

        byte[] bytes1 = mmkv.decodeBytes("bytes");

        System.out.println(new String(bytes1));


        //kv.removeValuesForKeys(new String[]{"int", "long"});
        mmkv.removeValueForKey("bytes");

        MMKV kv = MMKV.mmkvWithID("MyId");
        MMKV kv2 = MMKV.mmkvWithID("InterProcessKV",MMKV.MULTI_PROCESS_MODE);
        kv2.encode("key","key");
        kv.encode("key","key");




        //加密相关
        String cryptKey = "My-Encrypt-Key";
        MMKV cryptKv =  MMKV.mmkvWithID("mid",MMKV.MULTI_PROCESS_MODE,cryptKey);
        if (cryptKv != null) {
            cryptKv.reKey("wqpureuire");
        }



    }


}
