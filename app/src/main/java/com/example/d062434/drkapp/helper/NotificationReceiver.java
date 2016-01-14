package com.example.d062434.drkapp.helper;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.example.d062434.drkapp.R;
import com.example.d062434.drkapp.data.Profile;

import java.util.Calendar;

/**
 * Created by D062434 on 27.11.2015.
 */
public class NotificationReceiver extends WakefulBroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context, NotificationService.class));

    }
}
