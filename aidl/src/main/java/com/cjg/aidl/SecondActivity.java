package com.cjg.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author chenjinguang
 */
public class SecondActivity extends AppCompatActivity {

    private UserAidl userAidl;

    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            userAidl = UserAidl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = new Intent(this, MessageService.class);
        startService(intent);

        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void getUserName(View view){
        try {
            Toast.makeText(this,userAidl.getUserName(),Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public void getPassword(View view){
        try {
            Toast.makeText(this,userAidl.getUserPassword(),Toast.LENGTH_LONG).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
