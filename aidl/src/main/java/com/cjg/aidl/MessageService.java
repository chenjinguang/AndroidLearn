package com.cjg.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author chenjinguang
 * @描述
 * @创建时间 2021/1/25
 * @修改人和其它信息
 */
public class MessageService extends Service {


    private final UserAidl.Stub binder = new UserAidl.Stub() {
        @Override
        public String getUserName() throws RemoteException {
            Intent intent = new Intent(MessageService.this, SecondActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return "chenjinguang";
        }

        @Override
        public String getUserPassword() throws RemoteException {
            return "1988212";
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
