package com.example.d062434.drkapp;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d062434.drkapp.data.Geburtstag;
import com.example.d062434.drkapp.data.Mitglied;
import com.example.d062434.drkapp.data.Profile;
import com.example.d062434.drkapp.data.Termin;
import com.example.d062434.drkapp.data.Terminteilnahme;
import com.example.d062434.drkapp.helper.HTTPRequester;
import com.example.d062434.drkapp.helper.ListViewImageAdapter;
import com.example.d062434.drkapp.helper.StringOperator;
import com.example.d062434.drkapp.helper.Utility;

import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by D062434 on 16.10.2015.
 */
public class Fragment01 extends Fragment{
    private final int NFH_MIN_PERS = 2;

    private String[] data;
    private ArrayList<Termin> termine = new ArrayList<>();
    private ArrayList<Geburtstag> geburtstage = new ArrayList<>();
    private ArrayList<String> news = new ArrayList<>();
    private ArrayList<Mitglied> nfhBereitschaft = new ArrayList<>();
    private String lizenznummer;



    Fragment023 mFragment023 = new Fragment023();

    public Fragment01() {
        super();

    }

    public void setData(String[] data){
        this.data = data;
    }

    /* Datenextrahierung aus Response String */
    public void buildDataModel(){
        try{
            termine = new ArrayList<>();
            geburtstage = new ArrayList<>();
            news = new ArrayList<>();
            nfhBereitschaft = new ArrayList<>();

            lizenznummer = data[4];

            int counterGlobal = 5;
            int counterInner = 0;
            //Setting RechteIDs
            String id = "";
            while(data[counterGlobal].equals("Geburtstage") == false){
                id = data[counterGlobal];
                Profile.getRechteIds().add(id);
                counterGlobal++;
            }

            //Setting Birthdays
            String vorname = "";
            String nachname = "";
            String geburtstag = "";
            String alter = "";
            counterGlobal++;
            while(data[counterGlobal].equals("Termine") == false){
                switch(counterInner){
                    case 0: vorname = data[counterGlobal]; break;
                    case 1: nachname = data[counterGlobal]; break;
                    case 2: geburtstag = data[counterGlobal]; break;
                    case 3: alter = data[counterGlobal];
                        Geburtstag tempGeb = new Geburtstag(vorname, nachname, geburtstag, alter);
                        this.geburtstage.add(tempGeb);
                        counterInner = -1; break;
                }
                counterInner++;
                counterGlobal++;
            }
            counterGlobal++;
            counterInner = 0;
            //Setting Termine
            String terminName = "";
            String terminDatum ="";
            String terminID = "";
            int terminBesatzung = 0;
            int terminMinPers = 0;
            int terminMaxPers = 0;
            while (data[counterGlobal].equals("NfhBereitschaft") == false){
                switch(counterInner){
                    case 0: terminName = data[counterGlobal]; break;
                    case 1: terminDatum = data[counterGlobal]; break;
                    case 2: terminID = data[counterGlobal]; break;
                    case 3: terminBesatzung = Integer.parseInt(data[counterGlobal]); break;
                    case 4: terminMinPers = Integer.parseInt(data[counterGlobal]); break;
                    case 5: terminMaxPers = Integer.parseInt(data[counterGlobal]);
                        Termin tempTermin = new Termin(terminName, terminDatum, terminID, terminBesatzung, terminMinPers, terminMaxPers);
                        this.termine.add(tempTermin);
                        counterInner = -1; break;
                }
                counterInner++;
                counterGlobal++;
            }
            counterGlobal++;
            counterInner = 0;
            //Setting NFH Bereitschaft
            String nfhVName = "";
            String nfhNName = "";
            String nfhId = "";
            while (data[counterGlobal].equals("Nachricht") == false){
                switch(counterInner){
                    case 0: nfhVName = data[counterGlobal]; break;
                    case 1: nfhNName = data[counterGlobal]; break;
                    case 2: nfhId = data[counterGlobal];
                        Mitglied mgl = new Mitglied(nfhVName, nfhNName, nfhId);
                        this.nfhBereitschaft.add(mgl);
                        counterInner = -1; break;
                }
                counterInner++;
                counterGlobal++;
            }
            counterGlobal++;

            //Setting Nachricht
            while(counterGlobal < data.length){
                this.news.add(data[counterGlobal]);
                counterGlobal++;
            }
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }

    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Initialisierung des Startscreens
        View rootView = inflater.inflate(R.layout.fragment01, container, false);


        //Initialisierung des News-Frames
        TextView tweet = (TextView) rootView.findViewById(R.id.fragment01_textView_tweet);
        tweet.setText(this.news.get(0) + "\n" + this.news.get(1));
        TextView tweetAuthor = (TextView) rootView.findViewById(R.id.fragment01_textView_tweet_author);
        tweetAuthor.setText("Geschrieben von " + this.news.get(3) + " " + this.news.get(2));

        //Initialisierung des Notfallhilfe-Bereitschafts-Frames
        ListView lv1 = (ListView) rootView.findViewById(R.id.listView1);
        String lv1_values[] = new String[this.nfhBereitschaft.size()];
        for(int i = 0; i < this.nfhBereitschaft.size(); i++){
            lv1_values[i] = this.nfhBereitschaft.get(i).getVorname() + " " + this.nfhBereitschaft.get(i).getNachname();
        }
        ArrayAdapter<String> lv1_adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_fragment01_overview, R.id.listview_fragment01_overview_text, lv1_values);
        lv1.setAdapter(lv1_adapter);
        Utility.setListViewHeightBasedOnChildren(lv1);

        TextView personRequired = (TextView) rootView.findViewById(R.id.textView3);
        Button btnSignIn = (Button) rootView.findViewById(R.id.button1);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                for(int i = 0; i < nfhBereitschaft.size(); i++){
                    if(nfhBereitschaft.get(i).getId() == Profile.getId()){
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
                    else{
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

        //Initialisierung des Termine-Screens
        ListView lv2 = (ListView) rootView.findViewById(R.id.listView2);
        String lv2_values1[] = new String[this.termine.size()];
        String lv2_values3[] = new String[this.termine.size()];
        for(int i = 0; i < lv2_values1.length; i++){
            lv2_values1[i] = this.termine.get(i).getBezeichnung();
            lv2_values3[i] = this.termine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) + "." +
                                (this.termine.get(i).getBeginn().get(Calendar.MONTH)+1) + "." +
                                this.termine.get(i).getBeginn().get(Calendar.YEAR);
            if(this.termine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) != this.termine.get(i).getEnde().get(Calendar.DAY_OF_MONTH) ||
                    this.termine.get(i).getBeginn().get(Calendar.MONTH) != this.termine.get(i).getEnde().get(Calendar.MONTH) ||
                    this.termine.get(i).getBeginn().get(Calendar.YEAR) != this.termine.get(i).getEnde().get(Calendar.YEAR)){
                lv2_values3[i] += " - " + this.termine.get(i).getEnde().get(Calendar.DAY_OF_MONTH) + "." +
                        (this.termine.get(i).getEnde().get(Calendar.MONTH)+1) + "." +
                        this.termine.get(i).getEnde().get(Calendar.YEAR);
            }
        }

        int lv2_values2[] = new int[this.termine.size()];
        for(int i = 0; i < lv2_values2.length; i++){
            if(this.termine.get(i).getTeilnahme() == Terminteilnahme.NIEMAND){
                lv2_values2[i] = R.drawable.ter_rot;
            }
            else{
                if(this.termine.get(i).getTeilnahme() == Terminteilnahme.WENIGE){
                    lv2_values2[i] = R.drawable.ter_gelb;
                }
                else{
                    lv2_values2[i] = R.drawable.ter_gruen;
                }
            }
        }
        ListViewImageAdapter lv2_adapter = new ListViewImageAdapter(getActivity(), lv2_values1, lv2_values2, lv2_values3);
        lv2.setAdapter(lv2_adapter);
        Utility.setListViewHeightBasedOnChildren(lv2);

        //Wähle einen speziellen Termin aus
        lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Set Request Data for Backend Connection
                String[][] requestData = new String[1][2];
                requestData[0][0] = "terminId";
                requestData[0][1] = termine.get(position).getId();

                //Get Data from Backend
                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_termin.php");
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
                    mFragment023.setData(StringOperator.extractBetweenSpaces(response));
                    mFragment023.buildDataModel();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mFragment023).addToBackStack("app").commit();
                }
            }
        });

        //Initialisierung des Geburtstags-Frames
        ListView lv3 = (ListView) rootView.findViewById(R.id.listView3);
        String lv3_values[] = new String[this.geburtstage.size()];
        for(int i = 0; i < lv3_values.length; i++){
            lv3_values[i] = this.geburtstage.get(i).getVname() + " " + this.geburtstage.get(i).getNname() + " wird ";
            String[] tempGebDat = this.geburtstage.get(i).getGebdat().split(" ");
            this.geburtstage.get(i).setGebdat(tempGebDat[tempGebDat.length-1]);
            if(isToday(this.geburtstage.get(i).getGebdat())){
                lv3_values[i] += "heute ";
            }
            else{
                if(isTomorrow(this.geburtstage.get(i).getGebdat())){
                    lv3_values[i] += "morgen ";
                }
                else{
                    lv3_values[i] += "am " + this.geburtstage.get(i).getGebdat() + " ";
                }
            }

            lv3_values[i] += this.geburtstage.get(i).getAlter() + " Jahre alt";
        }
        ArrayAdapter<String> lv3_adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_fragment01_overview, R.id.listview_fragment01_overview_text, lv3_values);
        lv3.setAdapter(lv3_adapter);
        Utility.setListViewHeightBasedOnChildren(lv3);

        return rootView;
    }

    public boolean isToday(String gebDat){
        int day = Integer.parseInt(gebDat.substring(0,2));
        int month = Integer.parseInt(gebDat.substring(3,5)) - 1;
        int year = Integer.parseInt(gebDat.substring(6, 10));
        Calendar tempCalendar = Calendar.getInstance();
        if(tempCalendar.get(Calendar.DAY_OF_MONTH) == day &&
                tempCalendar.get(Calendar.MONTH) == month &&
                tempCalendar.get(Calendar.YEAR) == year){
            return true;
        }
        else{
          return false;
        }


    }

    public boolean isTomorrow(String gebDat){
        int day = Integer.parseInt(gebDat.substring(0,2));
        int month = Integer.parseInt(gebDat.substring(3,5)) - 1;
        int year = Integer.parseInt(gebDat.substring(6, 10));
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.add(Calendar.SECOND, 86400);
        if(tempCalendar.get(Calendar.DAY_OF_MONTH) == day &&
                tempCalendar.get(Calendar.MONTH) == month &&
                tempCalendar.get(Calendar.YEAR) == year){
            return true;
        }
        else{
            return false;
        }


    }

    public void updateFragment(){

        HTTPRequester myRequester = new HTTPRequester(null, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_login.php");
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
            Fragment01 mFragment01 = new Fragment01();
            mFragment01.setData(StringOperator.extractBetweenSpaces(response));
            mFragment01.buildDataModel();

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, mFragment01).addToBackStack("app").commit();
        }
    }



}
