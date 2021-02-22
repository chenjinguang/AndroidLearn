package com.cjg.hermeseventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cjg.hermeseventbus.ipc.entity.MessageEvent;

import xiaofei.library.hermeseventbus.HermesEventBus;

public class NextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

    }

    public void sendMessage(View view){
        HermesEventBus.getDefault().post(new MessageEvent("我是NextMessage传递的信息") );
        Toast.makeText(NextActivity.this, "成功发送消息！", Toast.LENGTH_SHORT).show();
    }
}