package com.cjg.hermeseventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cjg.hermeseventbus.ipc.entity.MessageEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import xiaofei.library.hermeseventbus.HermesEventBus;

public class MainActivity extends AppCompatActivity {

    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册
        HermesEventBus.getDefault().register(this);
        textView = findViewById(R.id.text);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HermesEventBus.getDefault().unregister(this);
    }

    public void goNext(View view) {
        startActivity(new Intent(this,NextActivity.class));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeMessage(MessageEvent messageEvent){

        String message = messageEvent.getMessage();

        textView.setText(message);

    }
}
