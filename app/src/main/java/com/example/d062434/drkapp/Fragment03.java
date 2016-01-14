package com.example.d062434.drkapp;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d062434.drkapp.Fragment023;
import com.example.d062434.drkapp.R;
import com.example.d062434.drkapp.data.Geburtstag;
import com.example.d062434.drkapp.data.Mitglied;
import com.example.d062434.drkapp.data.NfhEintrag;
import com.example.d062434.drkapp.data.Profile;
import com.example.d062434.drkapp.helper.CustomCalendarNfhView;
import com.example.d062434.drkapp.helper.CustomNFHDialog;
import com.example.d062434.drkapp.helper.CustomNFHTextView;
import com.example.d062434.drkapp.helper.HTTPRequester;
import com.example.d062434.drkapp.helper.ListViewImageAdapter;
import com.example.d062434.drkapp.helper.StringOperator;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by D062434 on 16.10.2015.
 */
public class Fragment03 extends Fragment{

    private final int NFH_MIN_PERS = 2;

    Fragment023 mFragment023 = new Fragment023();

    private CustomCalendarNfhView ccnfhv;

    private String data[];
    private ArrayList<Mitglied> nfhBereitschaftHeute;
    private ArrayList<Mitglied> nfhMitglieder;
    private ArrayList<NfhEintrag> nfhEintrag;

    public Fragment03() {
        super();

    }

    public void setData(String[] data){
        this.data = data;
    }

    public void buildDataModel(){
        this.nfhBereitschaftHeute = new ArrayList<>();
        this.nfhMitglieder = new ArrayList<>();
        this.nfhEintrag = new ArrayList<>();

        int counterGlobal = 1;
        int counterInner = 0;
        //Extract Data for todays NFH Bereitschaft
        String vorname = "";
        String nachname = "";
        String mglId = "";
        while(data[counterGlobal].equals("NfhPersonen") == false){
            switch(counterInner){
                case 0: vorname = data[counterGlobal]; break;
                case 1: nachname = data[counterGlobal]; break;
                case 2: mglId = data[counterGlobal];
                    Mitglied mgl = new Mitglied(vorname, nachname, mglId);
                    this.nfhBereitschaftHeute.add(mgl);
                    counterInner = -1; break;
            }
            counterInner++;
            counterGlobal++;
        }
        counterInner = 0;
        counterGlobal++;
        //Extract All NFH Mitglieder
        while(data[counterGlobal].equals("Wochenbereitschaft") == false){
            switch(counterInner){
                case 0: vorname = data[counterGlobal]; break;
                case 1: nachname = data[counterGlobal]; break;
                case 2: mglId = data[counterGlobal];
                    Mitglied mgl = new Mitglied(vorname, nachname, mglId);
                    this.nfhMitglieder.add(mgl);
                    counterInner = -1; break;
            }
            counterInner++;
            counterGlobal++;
        }
        counterInner = 0;
        counterGlobal++;
        //Extract NFH Inserts for the requested week
        Calendar tempCalendar = Calendar.getInstance();
        String status = "";
        while(counterGlobal < data.length){
            switch(counterInner){
                case 0: mglId = data[counterGlobal]; break;
                case 1: tempCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[counterGlobal].substring(8,10)));
                        tempCalendar.set(Calendar.MONTH, Integer.parseInt(data[counterGlobal].substring(5,7))-1);
                        tempCalendar.set(Calendar.YEAR, Integer.parseInt(data[counterGlobal].substring(0, 4))); break;
                case 2: status = data[counterGlobal];
                    Calendar calendar = (Calendar) tempCalendar.clone();
                    NfhEintrag eintrag = new NfhEintrag(mglId, calendar, status);
                    this.nfhEintrag.add(eintrag);
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

        View rootView = inflater.inflate(R.layout.fragment03, container, false);



        ListView lv1 = (ListView) rootView.findViewById(R.id.listView1);
        String lv1_values[] = new String[this.nfhBereitschaftHeute.size()];
        for(int i = 0; i < lv1_values.length; i++){
            lv1_values[i] = this.nfhBereitschaftHeute.get(i).getVorname() + " " + this.nfhBereitschaftHeute.get(i).getNachname();
        }
        ArrayAdapter<String> lv1_adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_fragment01_overview, R.id.listview_fragment01_overview_text, lv1_values);
        lv1.setAdapter(lv1_adapter);

        TextView personRequired = (TextView) rootView.findViewById(R.id.textView3);
        Button btnSignIn = (Button) rootView.findViewById(R.id.button1);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                for(int i = 0; i < nfhBereitschaftHeute.size(); i++){
                    if(nfhBereitschaftHeute.get(i).getId() == Profile.getId()){
                        //Don't insert a double NFH Bereitschaft
                        flag = false;
                    }
                }
                if(flag){
                    //Get Current Date
                    Calendar tempCalendar = Calendar.getInstance();
                    String rueck = "";
                    rueck += tempCalendar.get(Calendar.YEAR);
                    if(tempCalendar.get(Calendar.MONTH) < 9){
                        rueck += "0" + (tempCalendar.get(Calendar.MONTH)+1);
                    }
                    else{
                        rueck += (tempCalendar.get(Calendar.MONTH)+1);
                    }
                    if(tempCalendar.get(Calendar.DAY_OF_MONTH)< 10){
                        rueck += "0" + tempCalendar.get(Calendar.DAY_OF_MONTH);
                    }
                    else {
                        rueck += tempCalendar.get(Calendar.DAY_OF_MONTH);
                    }
                    //Set Request Data for Backend Connection
                    String[][] requestData = new String[2][2];
                    requestData[0][0] = "add_not_status";
                    requestData[0][1] = "B";
                    requestData[1][0] = "add_bereitschaft";
                    requestData[1][1] = "speichern";
                    //Get Data from Backend
                    String response = "";
                    try{
                        HTTPRequester myRequester = new HTTPRequester(requestData,
                                "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_notfallplaner.php?action=add&notdat=" +
                                        URLEncoder.encode(rueck, "UTF-8") + "&perid=" +
                                        URLEncoder.encode(Profile.getId(), "UTF-8") + "&vor=&zurueck=");

                        response = myRequester.execute().get();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    if(response.equals("")){
                        Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
                        tNoConnection.show();
                    }
                    else {
                        updateFragment();
                    }

                }
                else{
                    Toast tAlreadySignedIn = Toast.makeText(getContext(), "Sie haben sich bereits eingetragen.", Toast.LENGTH_LONG);
                    tAlreadySignedIn.show();
                }

            }
        });

        if(lv1_adapter.getCount() < NFH_MIN_PERS){
            personRequired.setVisibility(View.VISIBLE);
            btnSignIn.setVisibility(View.VISIBLE);
        }
        else {
            LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.linearLayout01);
            ll.removeView(personRequired);
            ll.removeView(btnSignIn);
        }

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.fragment03_calendar_nfh);

        ccnfhv = new CustomCalendarNfhView(getContext(),this);
        ll.addView(ccnfhv);



        final TextView tv_month = (TextView) rootView.findViewById(R.id.textView_month);
        tv_month.setText(ccnfhv.getCurrentMonth());

        Button btnNext = (Button) rootView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ccnfhv.showNextWeek(getContext());
                tv_month.setText(ccnfhv.getCurrentMonth());
            }
        });

        Button btnPrev = (Button) rootView.findViewById(R.id.btnPrev);
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ccnfhv.showPrevWeek(getContext());
                tv_month.setText(ccnfhv.getCurrentMonth());
            }
        });

        ImageView iv_legend1 = (ImageView) rootView.findViewById(R.id.fragment03_calendar_nfh_legend1);
        ImageView iv_legend2 = (ImageView) rootView.findViewById(R.id.fragment03_calendar_nfh_legend2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            iv_legend1.setBackground(ccnfhv.getAviableDrawable());
            iv_legend2.setBackground(ccnfhv.getNotAviableDrawable());
        }
        else{
            iv_legend1.setBackgroundDrawable(ccnfhv.getAviableDrawable());
            iv_legend2.setBackgroundDrawable(ccnfhv.getNotAviableDrawable());
        }

        return rootView;
    }

    public int getAnzNFHMitglieder(){
        return this.nfhMitglieder.size();
    }

    public Mitglied getNFHMitglied(int position){
        return this.nfhMitglieder.get(position);
    }

    public ArrayList<NfhEintrag> getNfhEintraege(){
        return this.nfhEintrag;
    }

    public void launchNFHDialog(String mglId, String mglName, String date, int hintergrundId){
        CustomNFHDialog nfhDialog = new CustomNFHDialog();
        nfhDialog.setMglId(mglId);
        nfhDialog.setDate(date);
        nfhDialog.setMglName(mglName);
        nfhDialog.setAction(hintergrundId);
        nfhDialog.setParentFragment(this);
        nfhDialog.show(getActivity().getSupportFragmentManager(), "NFHDialog");
    }

    public void sendRequest(Calendar newDate){
        //TODO: Verbessern des Datenaustauschs, nur NFH Kalender Daten holen und Update Funktion einführen
        //Get current date
        String year = "" + newDate.get(Calendar.YEAR);
        String month = "" + (newDate.get(Calendar.MONTH)+1);
        if(Integer.parseInt(month) < 10){
            month = "0" + month;
        }
        String day = "" + newDate.get(Calendar.DAY_OF_MONTH);
        if(Integer.parseInt(day) < 10){
            day = "0" +  day;
        }

        //Set Request Data for Backend Connection
        String[][] requestData = new String[2][2];
        requestData[0][0] = "dateStart";
        requestData[0][1] = year + month + day;
        requestData[1][0] = "dateEnd";
        requestData[1][1] = CustomCalendarNfhView.getEndOfWeek((Calendar) newDate.clone());

        //Get Data from Backend
        HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_einsatzScreen.php");
        String response = "";
        try{
            response = myRequester.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(response.equals("")){
            Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
            tNoConnection.show();
        }
        else{
            //Set the new Data
            this.setData(StringOperator.extractBetweenSpaces(response));
            this.buildDataModel();
        }
    }

    public void updateFragment(){
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

        if(response.equals("")){
            Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
            tNoConnection.show();
        }
        else{
            //Make a new instance to update the Fragment
            Fragment03 mFragment03 = new Fragment03();
            mFragment03.setData(StringOperator.extractBetweenSpaces(response));
            mFragment03.buildDataModel();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, mFragment03).addToBackStack("app").commit();
        }
    }
}
