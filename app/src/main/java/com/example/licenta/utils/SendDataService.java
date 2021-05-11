package com.example.licenta.utils;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

public class SendDataService extends Service {
    private final String TAG="Service";
    private Looper serviceLooper;
    protected ServiceHandler serviceHandler;
    protected Toast mToast;


    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {


  //      Utils.doWork();
        OncePerDayAppWorkExecutor probaTask = new OncePerDayAppWorkExecutor("Cosmin", 8, 4, 30);
        probaTask.start();

    }

    @Override
    public void onDestroy() {
        Log.d(TAG," s-a oprit service-ul");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG," a pornit service-ul");


//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                // ...
//                OncePerDayAppWorkExecutor probaTask = new OncePerDayAppWorkExecutor("Cosmin", 6, 34, 30);
//                probaTask.start();
//            }
//        });
//        return android.app.Service.START_STICKY;

        return START_STICKY;
    }

}