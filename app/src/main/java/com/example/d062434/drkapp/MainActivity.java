package com.example.d062434.drkapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d062434.drkapp.data.Profile;
import com.example.d062434.drkapp.helper.CustomCalendarNfhView;
import com.example.d062434.drkapp.helper.HTTPRequester;
import com.example.d062434.drkapp.helper.NotificationReceiver;
import com.example.d062434.drkapp.helper.NotificationService;
import com.example.d062434.drkapp.helper.StringOperator;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Fragment mFragment01 = new Fragment01(); //Start Screen
    private Fragment mFragment02 = new Fragment02(); //Appointment Screen
    private Fragment mFragment03 = new Fragment03(); //Mission Screen
    private Fragment mFragment99 = new Fragment99(); //Setting Screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get Data of PHP Request
        String[] startData = StringOperator.extractBetweenSpaces(getIntent().getStringExtra("responseStart"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set User Data
        Profile.setVname(startData[0]);
        Profile.setName(startData[1]);
        Profile.setId(startData[2]);

        SharedPreferences prefs = this.getSharedPreferences("com.example.drkapp", Context.MODE_PRIVATE);
        String mailKey = "com.example.drkapp.idKey";
        prefs.edit().putString(mailKey, startData[2]).apply();

        Profile.setEmail(prefs.getString("com.example.drkapp.mailKey", ""));

        //Initialisiere Navigation Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.inflateHeaderView(R.layout.nav_header_main);
        TextView tvUserName = (TextView) navHeader.findViewById(R.id.nav_tv1);
        tvUserName.setText(Profile.getVname() + " " + Profile.getName());

        TextView tvUserMail = (TextView) navHeader.findViewById(R.id.nav_tv2);
        tvUserMail.setText(Profile.getEmail());

        setUpAlarm(this.getApplication());


        //Initialisiere Daten für Start Fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        ((Fragment01)mFragment01).setData(startData);
        ((Fragment01) mFragment01).buildDataModel();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.container, mFragment01).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, mFragment99).addToBackStack("app").commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //Fragmentwechsel über Navigationdrawer findet hier statt.
        FragmentManager fragmentManager = getSupportFragmentManager();
        int id = item.getItemId();

        //Startscreen
        if (id == R.id.nav_start) {

            String[][] data = new String[3][2];
            data[0][0] = "Email";
            data[0][1] = Profile.getEmail();
            data[1][0] = "Passwort";
            data[1][1] = Profile.getPassword();
            data[2][0] = "login";
            data[2][1] = "Einloggen";

            HTTPRequester myRequester = new HTTPRequester(null, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_login.php");
            String response = "";
            try{
                response = myRequester.execute().get();
            }catch (Exception e){
                e.printStackTrace();
            }
            if(response.equals("")){
                Toast tNoConnection = Toast.makeText(this.getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                tNoConnection.show();
            }
            else{
                ((Fragment01)mFragment01).setData(StringOperator.extractBetweenSpaces(response));
                ((Fragment01)mFragment01).buildDataModel();
                fragmentManager.beginTransaction().replace(R.id.container, mFragment01).addToBackStack("app").commit();
            }



        //Termin Screen
        } else if (id == R.id.nav_appointments) {

            //Set Request Data for Backend Connection
            String[][] requestData = new String[3][2];
            requestData[0][0] = "userId";
            requestData[0][1] = Profile.getId();
            requestData[1][0] = "calendarDateThis";
            requestData[1][1] = CustomCalendarNfhView.getCalendarMonth(true);
            requestData[2][0] = "calendarDateNext";
            requestData[2][1] = CustomCalendarNfhView.getCalendarMonth(false);

            //Get Data from Backend
            HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_terminScreen.php");
            String response = "";
            try{
                response = myRequester.execute().get();
            }catch (Exception e){
                e.printStackTrace();
            }
            if (response.equals("")) {
                Toast tNoConnection = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                tNoConnection.show();
            }
            else{
                ((Fragment02)mFragment02).setData(StringOperator.extractBetweenSpaces(response));
                ((Fragment02)mFragment02).buildDataModel();
                fragmentManager.beginTransaction().replace(R.id.container, mFragment02).addToBackStack("app").commit();
            }

        //Einsatzscreen
        } else if (id == R.id.nav_missions) {
            //Get current date
            Calendar tempCalendar = Calendar.getInstance();
            String year = "" + tempCalendar.get(Calendar.YEAR);
            String month = "" + (tempCalendar.get(Calendar.MONTH)+1);
            if(Integer.parseInt(month) < 10){
                month = "0" + month;
            }
            String day = "" + tempCalendar.get(Calendar.DAY_OF_MONTH);
            if(Integer.parseInt(day) < 10){
                day = "0" +  day;
            }

            //Set Request Data for Backend Connection
            String[][] requestData = new String[2][2];
            requestData[0][0] = "dateStart";
            requestData[0][1] = year + month + day;
            requestData[1][0] = "dateEnd";
            requestData[1][1] = CustomCalendarNfhView.getEndOfWeek(tempCalendar);

            //Get Data from Backend
            HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_einsatzScreen.php");
            String response = "";
            try{
                response = myRequester.execute().get();
            }catch (Exception e){
                e.printStackTrace();
            }
            if (response.equals("")) {
                Toast tNoConnection = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG);
                tNoConnection.show();
            }else{
                ((Fragment03)mFragment03).setData(StringOperator.extractBetweenSpaces(response));
                ((Fragment03)mFragment03).buildDataModel();
                fragmentManager.beginTransaction().replace(R.id.container, mFragment03).commit();
            }

        //Statistikscreen
        } else if (id == R.id.nav_stats) {
            Toast tNoConnection = Toast.makeText(this.getApplicationContext(), "Aktuell nicht verfügbar", Toast.LENGTH_LONG);
            tNoConnection.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUpAlarm(Application context) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        //PendingIntent pending_intent = PendingIntent.getService(context, 0, intent, 0);
        PendingIntent pending_intent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarm_mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm_mgr.setRepeating(AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime(), NotificationService.duration, pending_intent);
    }

    @Override
    protected void onStop(){
        SharedPreferences prefs = this.getSharedPreferences("com.example.drkapp", Context.MODE_PRIVATE);
        prefs.edit().putString("com.example.drkapp.activeKey", "false").apply();
        String[][] requestData = new String[0][0];
        HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_logout.php");
        try{
            myRequester.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onStop();
    }

    @Override
    protected void onPause(){
        /*SharedPreferences prefs = this.getSharedPreferences("com.example.drkapp", Context.MODE_PRIVATE);
        prefs.edit().putString("com.example.drkapp.activeKey", "false");
        String[][] requestData = new String[0][0];
        HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_logout.php");
        try{
            myRequester.execute();
        }catch (Exception e){
            e.printStackTrace();
        }*/

        super.onPause();
    }

    @Override
    protected void onStart(){
        SharedPreferences prefs = this.getSharedPreferences("com.example.drkapp", Context.MODE_PRIVATE);
        prefs.edit().putString("com.example.drkapp.activeKey", "true").apply();

        String[][] data = new String[3][2];
        data[0][0] = "Email";
        data[0][1] = prefs.getString("com.example.drkapp.mailKey", "");
        data[1][0] = "Passwort";
        data[1][1] = prefs.getString("com.example.drkapp.logonKey", "");
        data[2][0] = "login";
        data[2][1] = "Einloggen";

        HTTPRequester myRequester = new HTTPRequester(data, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_login.php");
        try{
            myRequester.execute();
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onStart();
    }

}
