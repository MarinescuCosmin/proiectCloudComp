package com.example.licenta.utils;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class ExecWhenAppClosed extends IntentService {

    public ExecWhenAppClosed(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("IntentService","s-a apelat modificarea pe firestore");
        Utils.doWork();
    }
}
