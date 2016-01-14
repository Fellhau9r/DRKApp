package com.example.d062434.drkapp.helper;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.d062434.drkapp.MainActivity;
import com.example.d062434.drkapp.R;
import com.example.d062434.drkapp.data.Profile;

import java.util.Calendar;

/**
 * Created by D062434 on 24.11.2015.
 */
public class NotificationService extends Service{
    public static int duration = 1000 * 60 * 10;
    // Sets an ID for the notification
    int mNotificationId = 001;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        SharedPreferences prefs = this.getSharedPreferences("com.example.drkapp", Context.MODE_PRIVATE);

        String[][] data = new String[8][2];
        data[0][0] = "Email";
        data[0][1] = prefs.getString("com.example.drkapp.mailKey", "MobileUser");
        data[1][0] = "Passwort";
        data[1][1] = prefs.getString("com.example.drkapp.logonKey", "MobileCode");
        data[2][0] = "login";
        data[2][1] = "Einloggen";
        data[3][0] = "perId";
        data[3][1] = prefs.getString("com.example.drkapp.idKey", "MobileCode");
        data[4][0] = "not01";
        data[4][1] = prefs.getString("com.example.drkapp.not1Key", "true");
        data[5][0] = "not02";
        data[5][1] = prefs.getString("com.example.drkapp.not2Key", "true");
        data[6][0] = "not03";
        data[6][1] = prefs.getString("com.example.drkapp.not3Key", "true");
        data[7][0] = "not04";
        data[7][1] = prefs.getString("com.example.drkapp.not4Key", "true");

        HTTPRequester myRequester = new HTTPRequester(data, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_notificationManager.php");
        String response = "";
        try{
            response = myRequester.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        if(response.equals("") || response.equals("new") || response.equals("authentification error")){
            //Do Nothing
        }
        else{
            decodeResponse(StringOperator.extractBetweenSpaces(response));
        }
        if(prefs.getString("com.example.drkapp.activeKey", "false").equals("false")){
            String[][] requestData = new String[0][0];
            HTTPRequester myRequester2 = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_logout.php");
            try{
                myRequester2.execute();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void decodeResponse(String[] data){


        int counterGlobal = 1;
        int counterInner = 0;

        //Setting Notification Type 1 (Termin Reminder)
        String terId = "";
        String terName = "";
        String terBeginn = "";
        while(data[counterGlobal].equals("not2") == false){
            switch(counterInner){
                case 0: terId = data[counterGlobal]; break;
                case 1: terName = data[counterGlobal]; break;
                case 2: terBeginn = data[counterGlobal];
                    Calendar beginn = Calendar.getInstance();
                    beginn.set(Calendar.YEAR, Integer.parseInt(terBeginn.substring(0, 4)));
                    beginn.set(Calendar.MONTH, Integer.parseInt(terBeginn.substring(5, 7)));
                    beginn.set(Calendar.DAY_OF_MONTH, Integer.parseInt(terBeginn.substring(8, 10)));
                    beginn.set(Calendar.HOUR_OF_DAY, Integer.parseInt(terBeginn.substring(11, 13)));
                    beginn.set(Calendar.MINUTE, Integer.parseInt(terBeginn.substring(14, 16)));
                    String title = "Terminerinnerung";
                    String content = terName + " beginnt um " +
                                        beginn.get(Calendar.HOUR_OF_DAY) + ":" +
                                        String.format("%02d", beginn.get(Calendar.MINUTE)) + " Uhr.";
                    pushNotification(title, content);

                    counterInner = -1; break;
            }
            counterInner++;
            counterGlobal++;
        }
        counterGlobal++;
        counterInner = 0;

        //Setting Notification Type 2 (Person signed into same Termin)
        String mglId = "";
        String mglVorname = "";
        String mglNachname = "";
        while(data[counterGlobal].equals("not3") == false){
            switch(counterInner){
                case 0: terId = data[counterGlobal]; break;
                case 1: terName = data[counterGlobal]; break;
                case 2: mglId = data[counterGlobal]; break;
                case 3: mglVorname = data[counterGlobal]; break;
                case 4: mglNachname = data[counterGlobal];
                    String title = "Termineintrag";
                    String content = mglVorname + " " + mglNachname + " hat sich ebenfalls in den" +
                                        " Termin " + terName + " eingetragen.";
                    pushNotification(title, content);

                    counterInner = -1; break;
            }
            counterInner++;
            counterGlobal++;
        }
        counterGlobal++;
        counterInner = 0;

        //Setting Notification Type 3 (Person signed into same NFH Bereitschaft)
        String notDatum = "";
        while(data[counterGlobal].equals("not4") == false){
            switch(counterInner){
                case 0: notDatum = data[counterGlobal]; break;
                case 1: mglId = data[counterGlobal]; break;
                case 2: mglVorname = data[counterGlobal]; break;
                case 3: mglNachname = data[counterGlobal];
                    Calendar date = Calendar.getInstance();
                    date.set(Calendar.YEAR, Integer.parseInt(notDatum.substring(0, 4)));
                    date.set(Calendar.MONTH, Integer.parseInt(notDatum.substring(5, 7)));
                    date.set(Calendar.DAY_OF_MONTH, Integer.parseInt(notDatum.substring(8, 10)));
                    String title = "Nachtschichtplanung";
                    String content = mglVorname + " " + mglNachname + " macht am " +
                                        date.get(Calendar.DAY_OF_MONTH) + "." +
                                        date.get(Calendar.MONTH) + "." +
                                        date.get(Calendar.YEAR) + " mit dir zusammen Nachtschicht.";
                    pushNotification(title, content);
                    counterInner = -1; break;
            }
            counterInner++;
            counterGlobal++;
        }
        counterGlobal++;
        counterInner = 0;

    }

    public void pushNotification(String title, String content){
        SharedPreferences prefs = this.getSharedPreferences("com.example.drkapp", Context.MODE_PRIVATE);
        String[][] data = new String[3][2];
        data[0][0] = "Email";
        data[0][1] = prefs.getString("com.example.drkapp.mailKey", "");
        data[1][0] = "Passwort";
        data[1][1] = prefs.getString("com.example.drkapp.logonKey", "");
        data[2][0] = "login";
        data[2][1] = "Einloggen";

        HTTPRequester myRequester = new HTTPRequester(data, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_login.php");
        String responseStart = "";
        try{
            responseStart = myRequester.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }
        Intent onClickIntent = new Intent(this, MainActivity.class);
        onClickIntent.putExtra("responseStart", responseStart);
        onClickIntent.putExtra("email", prefs.getString("com.example.drkapp.mailKey", ""));

        PendingIntent onClickPendingIntent = PendingIntent.getActivity(this, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Context context = this;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.logo_mdpi)
                        .setContentTitle(title)
                        .setContentText(content);
        mBuilder.setContentIntent(onClickPendingIntent);


        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
        mNotificationId++;

    }
}
