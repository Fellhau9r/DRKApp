package com.example.d062434.drkapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d062434.drkapp.data.Geburtstag;
import com.example.d062434.drkapp.data.Profile;
import com.example.d062434.drkapp.data.Termin;
import com.example.d062434.drkapp.data.Terminteilnahme;
import com.example.d062434.drkapp.helper.CustomCalendarNfhView;
import com.example.d062434.drkapp.helper.CustomCalendarView;
import com.example.d062434.drkapp.helper.HTTPRequester;
import com.example.d062434.drkapp.helper.ListViewImageAdapter;
import com.example.d062434.drkapp.helper.StringOperator;
import com.example.d062434.drkapp.helper.Utility;
import com.example.d062434.drkapp.listener.OnSwipeTouchListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by D062434 on 16.10.2015.
 */
public class Fragment02 extends Fragment{
    private Fragment022 mFragment022 = new Fragment022();
    private Fragment023 mFragment023 = new Fragment023();
    private CustomCalendarView calendarView;

    private String[] data ;
    private ArrayList<Termin> monthTermine = new ArrayList<>();
    private ArrayList<Termin> userTermine = new ArrayList<>();

    public Fragment02() {
        super();

    }

    public void setData(String[] data){
        this.data = data;
    }

    public void buildDataModel(){

        monthTermine = new ArrayList<>();
        userTermine = new ArrayList<>();

        int counterGlobal = 1;
        int counterInner = 0;
        //Setting User Termine
        String terminName = "";
        String terminDatum ="";
        String terminID = "";
        int terminBesatzung = 0;
        int terminMinPers = 0;
        int terminMaxPers = 0;

        while(data[counterGlobal].equals("MonthTermine") == false){
            switch(counterInner){
                case 0: terminName = data[counterGlobal]; break;
                case 1: terminDatum = data[counterGlobal]; break;
                case 2: terminID = data[counterGlobal]; break;
                case 3: terminBesatzung = Integer.parseInt(data[counterGlobal]); break;
                case 4: terminMinPers = Integer.parseInt(data[counterGlobal]); break;
                case 5: terminMaxPers = Integer.parseInt(data[counterGlobal]);
                    Termin tempTermin = new Termin(terminName, terminDatum, terminID, terminBesatzung, terminMinPers, terminMaxPers);
                    this.userTermine.add(tempTermin);
                    counterInner = -1; break;
            }
            counterInner++;
            counterGlobal++;
        }
        counterGlobal++;
        counterInner = 0;
        //Setting Termine

        while (counterGlobal < data.length){
            switch(counterInner){
                case 0: terminName = data[counterGlobal]; break;
                case 1: terminDatum = data[counterGlobal]; break;
                case 2: terminID = data[counterGlobal]; break;
                case 3: terminBesatzung = Integer.parseInt(data[counterGlobal]); break;
                case 4: terminMinPers = Integer.parseInt(data[counterGlobal]); break;
                case 5: terminMaxPers = Integer.parseInt(data[counterGlobal]);
                    Termin tempTermin = new Termin(terminName, terminDatum, terminID, terminBesatzung, terminMinPers, terminMaxPers);
                    this.monthTermine.add(tempTermin);
                    counterInner = -1; break;
            }
            counterInner++;
            counterGlobal++;
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment02, container, false);

        //Set User Termin Frame
        ListView lv1 = (ListView) rootView.findViewById(R.id.listView1);

        String lv1_values1[] = new String[this.userTermine.size()];
        String lv1_values3[] = new String[this.userTermine.size()];
        for(int i = 0; i < lv1_values1.length; i++){
            lv1_values1[i] = this.userTermine.get(i).getBezeichnung();
            lv1_values3[i] = this.userTermine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) + "." +
                    (this.userTermine.get(i).getBeginn().get(Calendar.MONTH)+1) + "." +
                    this.userTermine.get(i).getBeginn().get(Calendar.YEAR);
            if(this.userTermine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) != this.userTermine.get(i).getEnde().get(Calendar.DAY_OF_MONTH) ||
                    this.userTermine.get(i).getBeginn().get(Calendar.MONTH) != this.userTermine.get(i).getEnde().get(Calendar.MONTH) ||
                    this.userTermine.get(i).getBeginn().get(Calendar.YEAR) != this.userTermine.get(i).getEnde().get(Calendar.YEAR)){
                lv1_values3[i] += " - " + this.userTermine.get(i).getEnde().get(Calendar.DAY_OF_MONTH) + "." +
                        (this.userTermine.get(i).getEnde().get(Calendar.MONTH)+1) + "." +
                        this.userTermine.get(i).getEnde().get(Calendar.YEAR);
            }
        }
        int lv1_values2[] = new int[this.userTermine.size()];
        for(int i = 0; i < lv1_values2.length; i++){
            if(this.userTermine.get(i).getTeilnahme() == Terminteilnahme.NIEMAND){
                lv1_values2[i] = R.drawable.ter_rot;
            }
            else{
                if(this.userTermine.get(i).getTeilnahme() == Terminteilnahme.WENIGE){
                    lv1_values2[i] = R.drawable.ter_gelb;
                }
                else{
                    lv1_values2[i] = R.drawable.ter_gruen;
                }
            }
        }
        ListViewImageAdapter adapter = new ListViewImageAdapter(getActivity(), lv1_values1, lv1_values2, lv1_values3);
        lv1.setAdapter(adapter);

        //Wähle einen speziellen Termin aus
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set Request Data for Backend Connection
                String[][] requestData = new String[1][2];
                requestData[0][0] = "terminId";
                requestData[0][1] = userTermine.get(position).getId();

                //Get Data from Backend
                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_termin.php");
                String response = "";
                try{
                    response = myRequester.execute().get();
                }catch (Exception e){
                    e.printStackTrace();
                }

                mFragment023.setData(StringOperator.extractBetweenSpaces(response));
                mFragment023.buildDataModel();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container, mFragment023).addToBackStack("app").commit();
            }
        });

        //Öffne Listview mit allen kommenden Terminen
        Button btnAllDates = (Button) rootView.findViewById(R.id.button1);
        btnAllDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                //Get Data from Backend
                HTTPRequester myRequester = new HTTPRequester(null, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_allTermine.php");
                String response = "";
                try{
                    response = myRequester.execute().get();
                }catch (Exception e){
                    e.printStackTrace();
                }

                mFragment022.setData(StringOperator.extractBetweenSpaces(response));
                mFragment022.buildDataModel();
                fragmentManager.beginTransaction().replace(R.id.container, mFragment022).addToBackStack("app").commit();
            }
        });

        calendarView = (CustomCalendarView) rootView.findViewById(R.id.calendarview);
        calendarView.setMonthTermine(this.monthTermine);
        calendarView.setParentFragment(this);
        calendarView.invalidate();
        Calendar monthCalendar = calendarView.getCalendar();
        final TextView tv_month = (TextView) rootView.findViewById(R.id.textView_month);
        tv_month.setText(getDateString(monthCalendar));
        Button btnPrev = (Button) rootView.findViewById(R.id.btnPrev);
        Button btnNext = (Button) rootView.findViewById(R.id.btnNext);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Get Calendar Date
                Calendar tempCalendar = (Calendar) calendarView.getCalendar().clone();
                tempCalendar.add(Calendar.MONTH, -1);
                String rueck = "";
                rueck += tempCalendar.get(Calendar.YEAR);
                if(tempCalendar.get(Calendar.MONTH) < 9){
                    rueck += "0" + (tempCalendar.get(Calendar.MONTH)+1);
                }
                else{
                    rueck += (tempCalendar.get(Calendar.MONTH)+1);
                }

                //Set Request Data for Backend Connection
                String[][] requestData = new String[3][2];
                requestData[0][0] = "userId";
                requestData[0][1] = Profile.getId();
                requestData[1][0] = "calendarDateThis";
                requestData[1][1] = rueck;


                tempCalendar.add(Calendar.MONTH, 1);
                rueck = "" + tempCalendar.get(Calendar.YEAR);
                if(tempCalendar.get(Calendar.MONTH) < 9){
                    rueck += "0" + (tempCalendar.get(Calendar.MONTH)+1);
                }
                else{
                    rueck += (tempCalendar.get(Calendar.MONTH)+1);
                }
                requestData[2][0] = "calendarDateNext";
                requestData[2][1] = rueck;

                //Get Data from Backend
                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_terminScreen.php");
                String response = "";
                try{
                    response = myRequester.execute().get();
                }catch (Exception e){
                    e.printStackTrace();
                }

                setData(StringOperator.extractBetweenSpaces(response));
                buildDataModel();

                calendarView.setMonthTermine(monthTermine);
                calendarView.showLastsMonth();
                Calendar calendar = calendarView.getCalendar();
                tv_month.setText(getDateString(calendar));
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                //Get Calendar Date
                Calendar tempCalendar = (Calendar) calendarView.getCalendar().clone();
                tempCalendar.add(Calendar.MONTH, 1);
                String rueck = "";
                rueck += tempCalendar.get(Calendar.YEAR);
                if(tempCalendar.get(Calendar.MONTH) < 9){
                    rueck += "0" + (tempCalendar.get(Calendar.MONTH)+1);
                }
                else{
                    rueck += (tempCalendar.get(Calendar.MONTH)+1);
                }

                //Set Request Data for Backend Connection
                String[][] requestData = new String[3][2];
                requestData[0][0] = "userId";
                requestData[0][1] = Profile.getId();
                requestData[1][0] = "calendarDateThis";
                requestData[1][1] = rueck;


                tempCalendar.add(Calendar.MONTH, 1);
                rueck = "" + tempCalendar.get(Calendar.YEAR);
                if(tempCalendar.get(Calendar.MONTH) < 9){
                    rueck += "0" + (tempCalendar.get(Calendar.MONTH)+1);
                }
                else{
                    rueck += (tempCalendar.get(Calendar.MONTH)+1);
                }
                requestData[2][0] = "calendarDateNext";
                requestData[2][1] = rueck;

                //Get Data from Backend
                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_terminScreen.php");
                String response = "";
                try{
                    response = myRequester.execute().get();
                }catch (Exception e){
                    e.printStackTrace();
                }

                setData(StringOperator.extractBetweenSpaces(response));
                buildDataModel();

                calendarView.setMonthTermine(monthTermine);
                calendarView.showNextMonth();
                Calendar calendar = calendarView.getCalendar();
                tv_month.setText(getDateString(calendar));
            }
        });


        calendarView.setOnDateSelectedListener(new CustomCalendarView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar) {
                Log.e("TEST", "selected date is : " + getDateString2(calendar));
            }
        });

        Calendar calendar = Calendar.getInstance();
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.add(Calendar.MONTH, -2);
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.add(Calendar.MONTH, 2);

        calendarView.setMinMaxCalendar(minCalendar, maxCalendar);
        calendarView.setCalendar(calendar);

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.fragment02_calendar_layout);
        ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        return rootView;
    }

    private String getDateString(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(calendar.getTime());
    }

    private String getDateString2(Calendar calendar) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(calendar.getTime());
    }


}
