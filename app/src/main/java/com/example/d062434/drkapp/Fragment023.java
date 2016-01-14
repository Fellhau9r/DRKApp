package com.example.d062434.drkapp;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.d062434.drkapp.data.Mitglied;
import com.example.d062434.drkapp.data.Profile;
import com.example.d062434.drkapp.data.Termin;
import com.example.d062434.drkapp.data.Terminkategorie;
import com.example.d062434.drkapp.helper.CustomPersonalView;
import com.example.d062434.drkapp.helper.HTTPRequester;
import com.example.d062434.drkapp.helper.ListAviablePeopleTerminAdapter;
import com.example.d062434.drkapp.helper.ListPersonTerminAdapter;
import com.example.d062434.drkapp.helper.ListViewImageAdapter;
import com.example.d062434.drkapp.helper.StringOperator;
import com.example.d062434.drkapp.helper.Utility;

import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by D062434 on 25.10.2015.
 */
public class Fragment023 extends Fragment{
    private Termin termin;
    private ArrayList<Mitglied> aviablePeople = new ArrayList<>();

    private String data[];
    private Calendar aenderungBegZeit = Calendar.getInstance();
    private Calendar aenderungEndZeit = Calendar.getInstance();
    private String aenderungPerId = "";


    public Fragment023() {
        super();

    }

    public void setData(String[] data){
        this.data = data;
    }

    public void setTermin(Termin newTermin){
        this.termin = newTermin;
    }

    public void buildDataModel(){
        Terminkategorie terKat;
        boolean statRel = true;
        boolean persUnbeg = false;
        Calendar calendarBeg = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();

        //Terminkategorie prüfen
        switch(Integer.parseInt(this.data[1])){
            case 1: terKat = Terminkategorie.AUSBILDUNG; break;
            case 2: terKat = Terminkategorie.UEBUNGSEINHEIT; break;
            case 3: terKat = Terminkategorie.LEHRGANG; break;
            case 4: terKat = Terminkategorie.BEREITSCHAFT; break;
            case 5: terKat = Terminkategorie.BLUTSPENDEN; break;
            case 6: terKat = Terminkategorie.SANDIENST; break;
            case 7: terKat = Terminkategorie.TURNIERTEILNAHME; break;
            case 8: terKat = Terminkategorie.FESTVERANSTALTUNG; break;
            case 9: terKat = Terminkategorie.BESPRECHUNG; break;
            default: terKat = Terminkategorie.SONSTIGES; break;
        }
        //Statistikrelevanz prüfen
        if(this.data[4] == "N"){
            statRel = false;
        }

        //Beginn und Ende ermitteln
        calendarBeg.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[8].substring(0,2)));
        calendarBeg.set(Calendar.MONTH, Integer.parseInt(data[8].substring(3, 5)) - 1);
        calendarBeg.set(Calendar.YEAR, Integer.parseInt("20".concat(data[8].substring(6, 8))));
        calendarBeg.set(Calendar.HOUR_OF_DAY, Integer.parseInt(data[6].substring(0, 2)));
        calendarBeg.set(Calendar.MINUTE, Integer.parseInt(data[6].substring(3, 5)));
        //calendarBeg.set(Calendar.SECOND, Integer.parseInt(data[6].substring(6,8)));

        calendarEnd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(data[9].substring(0,2)));
        calendarEnd.set(Calendar.MONTH, Integer.parseInt(data[9].substring(3,5))-1);
        calendarEnd.set(Calendar.YEAR, Integer.parseInt("20".concat(data[9].substring(6, 8))));
        calendarEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(data[7].substring(0, 2)));
        calendarEnd.set(Calendar.MINUTE, Integer.parseInt(data[7].substring(3, 5)));
        //calendarEnd.set(Calendar.SECOND, Integer.parseInt(data[7].substring(6,8)));

        //Unbegrenzte Teilnehmerzahl prüfen
        if(Integer.parseInt(this.data[12]) == 99999){
            persUnbeg = true;
        }

        this.termin = new Termin(this.data[0], terKat, this.data[2], this.data[3],statRel,
                this.data[5], calendarBeg, calendarEnd, Integer.parseInt(this.data[11]),
                Integer.parseInt(this.data[12]), persUnbeg);

        int counterGlobal = 14;
        int counterInner = 0;
        String id = "";
        String vorname = "";
        String nachname = "";
        Calendar mglBeg = Calendar.getInstance();
        Calendar mglEnd = Calendar.getInstance();
        while(counterGlobal < this.data.length){
            switch(counterInner){
                case 0: id = this.data[counterGlobal]; break;
                case 1: vorname = this.data[counterGlobal]; break;
                case 2: nachname = this.data[counterGlobal]; break;
                case 3: if(this.data[counterGlobal].equals("") == false){
                            mglBeg.set(Calendar.YEAR, Integer.parseInt(this.data[counterGlobal].substring(0,4)));
                            mglBeg.set(Calendar.MONTH, Integer.parseInt(this.data[counterGlobal].substring(5,7))-1);
                            mglBeg.set(Calendar.DAY_OF_MONTH, Integer.parseInt(this.data[counterGlobal].substring(8,10)));
                        }
                        else{
                            mglBeg.set(Calendar.YEAR, calendarBeg.get(Calendar.YEAR));
                            mglBeg.set(Calendar.MONTH, calendarBeg.get(Calendar.MONTH));
                            mglBeg.set(Calendar.DAY_OF_MONTH, calendarBeg.get(Calendar.DAY_OF_MONTH));
                        }
                        break;
                case 4: if(this.data[counterGlobal].equals("") == false){
                            mglEnd.set(Calendar.YEAR, Integer.parseInt(this.data[counterGlobal].substring(0,4)));
                            mglEnd.set(Calendar.MONTH, Integer.parseInt(this.data[counterGlobal].substring(5,7))-1);
                            mglEnd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(this.data[counterGlobal].substring(8,10)));
                        }
                        else{
                            mglEnd.set(Calendar.YEAR, calendarEnd.get(Calendar.YEAR));
                            mglEnd.set(Calendar.MONTH, calendarEnd.get(Calendar.MONTH));
                            mglEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.get(Calendar.DAY_OF_MONTH));
                        }
                        break;
                case 5: if(this.data[counterGlobal].equals("") == false){
                            mglBeg.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.data[counterGlobal].substring(0,2)));
                            mglBeg.set(Calendar.MINUTE, Integer.parseInt(this.data[counterGlobal].substring(3,5)));
                        }
                        else{
                            mglBeg.set(Calendar.HOUR_OF_DAY, calendarBeg.get(Calendar.HOUR_OF_DAY));
                            mglBeg.set(Calendar.MINUTE, calendarBeg.get(Calendar.MINUTE));
                        }
                        break;
                case 6: if(this.data[counterGlobal].equals("") == false){
                            mglEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(this.data[counterGlobal].substring(0,2)));
                            mglEnd.set(Calendar.MINUTE, Integer.parseInt(this.data[counterGlobal].substring(3, 5)));
                        }
                        else{
                            mglEnd.set(Calendar.HOUR_OF_DAY, calendarEnd.get(Calendar.HOUR_OF_DAY));
                            mglEnd.set(Calendar.MINUTE, calendarEnd.get(Calendar.MINUTE));
                        }
                        Mitglied mitglied = new Mitglied(id, vorname, nachname, (Calendar) mglBeg.clone(), (Calendar) mglEnd.clone());
                        this.termin.getTeilnehmer().add(mitglied);
                        counterInner = -1;
                        break;
            }

            counterInner++;
            counterGlobal++;
        }
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment023, container, false);


        // Set Table Content
        TextView tv1 = (TextView) rootView.findViewById(R.id.TextView01);
        TextView tv2 = (TextView) rootView.findViewById(R.id.textView4);
        TextView tv3 = (TextView) rootView.findViewById(R.id.textView8);
        TextView tv4 = (TextView) rootView.findViewById(R.id.textView10);
        TextView tv5 = (TextView) rootView.findViewById(R.id.textView12);
        TextView tv6 = (TextView) rootView.findViewById(R.id.textView14);
        TextView tv7 = (TextView) rootView.findViewById(R.id.textView16);


        tv2.setText(termin.getKategorie());
        tv1.setText(termin.getBezeichnung());
        tv3.setText(termin.getBeschreibung());
        tv4.setText(termin.getOrt());
        Calendar tempCalendar = (Calendar) termin.getBeginn().clone();
        String terminDate = "" + tempCalendar.get(Calendar.DAY_OF_MONTH) + "." +
                            (tempCalendar.get(Calendar.MONTH)+1) + "." +
                            tempCalendar.get(Calendar.YEAR);
        String terminTime = String.format("%02d", tempCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                            String.format("%02d", tempCalendar.get(Calendar.MINUTE));
        tempCalendar = (Calendar) termin.getEnde().clone();
        terminDate += " - " + tempCalendar.get(Calendar.DAY_OF_MONTH) + "." +
                        (tempCalendar.get(Calendar.MONTH)+1) + "." +
                        tempCalendar.get(Calendar.YEAR);
        terminTime += " - " + String.format("%02d", tempCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                        String.format("%02d", tempCalendar.get(Calendar.MINUTE));
        tv5.setText(terminDate);
        tv6.setText(terminTime);
        if(termin.isStatistikrelevanz()){
            tv7.setText("Ja");
        }
        else{
            tv7.setText("Nein");
        }

        //Set Status Bar (Diagramm which shows the amount of people participating)
        CustomPersonalView cpv = (CustomPersonalView) rootView.findViewById(R.id.customPersonalView);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        cpv.setScreenSize(metrics.widthPixels);
        cpv.setPersonal(termin.getMinPers(), termin.getMaxPers(), termin.getTeilnehmer().size());
        cpv.invalidate();

        //If there are too less people signed in
        TextView tv18 = (TextView) rootView.findViewById(R.id.textView18);
        if(termin.getMinPers() > termin.getCurrentPers()){
            tv18.setText("Es werden noch Personen für diesen Termin benötigt.");
        }

        //Get All People participating at this appointment
        ListView lv1 = (ListView) rootView.findViewById(R.id.listView);
        String lv1_values1[] = new String[termin.getTeilnehmer().size()];
        String lv1_values2[] = new String[termin.getTeilnehmer().size()];
        for(int i = 0; i < lv1_values1.length; i++){
            lv1_values1[i] = termin.getTeilnehmer().get(i).getVorname() + " " + termin.getTeilnehmer().get(i).getNachname();
            tempCalendar = (Calendar) termin.getTeilnehmer().get(i).getBegZeit().clone();
            lv1_values2[i] = "" + tempCalendar.get(Calendar.DAY_OF_MONTH) + "." +
                                (tempCalendar.get(Calendar.MONTH)+1) + "." +
                                tempCalendar.get(Calendar.YEAR) + " " +
                                String.format("%02d", tempCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                                String.format("%02d", tempCalendar.get(Calendar.MINUTE));
            tempCalendar = (Calendar) termin.getTeilnehmer().get(i).getEndZeit().clone();
            lv1_values2[i] += " - " + tempCalendar.get(Calendar.DAY_OF_MONTH) + "." +
                    (tempCalendar.get(Calendar.MONTH)+1) + "." +
                    tempCalendar.get(Calendar.YEAR) + " " +
                    String.format("%02d", tempCalendar.get(Calendar.HOUR_OF_DAY)) + ":" +
                    String.format("%02d", tempCalendar.get(Calendar.MINUTE));
        }
        ListPersonTerminAdapter lv1_adapter = new ListPersonTerminAdapter(getActivity(), lv1_values1, lv1_values2);
        lv1.setAdapter(lv1_adapter);
        Utility.setListViewHeightBasedOnChildren(lv1);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aenderungPerId = termin.getTeilnehmer().get(position).getId();
                if (aenderungPerId.equals(Profile.getId()) || Profile.getRechteIds().contains("6")) {
                    String clickedPerson = termin.getTeilnehmer().get(position).getNachname() + " " + termin.getTeilnehmer().get(position).getVorname();
                    if (termin.getBeginn().get(Calendar.YEAR) == termin.getEnde().get(Calendar.YEAR) &&
                            termin.getBeginn().get(Calendar.MONTH) == termin.getEnde().get(Calendar.MONTH) &&
                            termin.getBeginn().get(Calendar.DAY_OF_MONTH) == termin.getEnde().get(Calendar.DAY_OF_MONTH)) {
                        aenderungBegZeit.set(Calendar.YEAR, termin.getBeginn().get(Calendar.YEAR));
                        aenderungBegZeit.set(Calendar.MONTH, termin.getBeginn().get(Calendar.MONTH));
                        aenderungBegZeit.set(Calendar.DAY_OF_MONTH, termin.getBeginn().get(Calendar.DAY_OF_MONTH));
                        openTimePickerDialogBeg(clickedPerson);
                    } else {
                        openDatePickerDialogBeg(clickedPerson);
                    }
                }
            }
        });

        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Mitglied person = termin.getTeilnehmer().get(position);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.cancel();
                                //Set Request Data for Backend Connection
                                String[][] requestData = new String[3][2];
                                requestData[0][0] = "action";
                                requestData[0][1] = "del";
                                requestData[1][0] = "terId";
                                requestData[1][1] = termin.getId();
                                requestData[2][0] = "perId";
                                requestData[2][1] = person.getId();

                                //Get Data from Backend
                                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_deletePerTermin.php");
                                String response = "";
                                try {
                                    response = myRequester.execute().get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if(response.equals("")){
                                    Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
                                    tNoConnection.show();
                                }
                                else{
                                    if(response.equals("1")){
                                        Toast tNoConnection = Toast.makeText(getContext(), "Berechtigungsfehler", Toast.LENGTH_LONG);
                                        tNoConnection.show();
                                    }
                                    else{
                                        if(response.equals("ok")){
                                            updateFragment();
                                        }
                                        else{
                                            Toast tNoConnection = Toast.makeText(getContext(), "Fehler bei der Übertragung. Melden Sie sich bitte erneut an.", Toast.LENGTH_LONG);
                                            tNoConnection.show();
                                        }
                                    }
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Möchten Sie " + person.getVorname() + " " + person.getNachname() + " aus dem Termin austragen?")
                        .setPositiveButton("Person austragen", dialogClickListener)
                        .setNegativeButton("Abbrechen", dialogClickListener).show();
                return true;
            }
        });

        //Look if User is Signed in Appointment
        boolean flag = false;
        for(int i = 0; i < this.termin.getTeilnehmer().size(); i++){
            if(this.termin.getTeilnehmer().get(i).getId().equals(Profile.getId())){
                flag = true;
            }
        }
        final boolean isUserSignedIn = flag;
        //Get the Buttons (SignIn/SingOut and ChangeTime) and handle these
        Button btnSignIn = (Button) rootView.findViewById(R.id.btnSignIn);


        if(isUserSignedIn){
            btnSignIn.setText("Austragen");
        }
        else{
            btnSignIn.setText("Eintragen");
        }
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Set Request Data for Backend Connection
                String[][] requestData = new String[3][2];
                requestData[1][0] = "mglId";
                requestData[1][1] = Profile.getId();
                requestData[2][0] = "terId";
                requestData[2][1] = termin.getId();
                if(isUserSignedIn){
                    //Sign Out
                    requestData[0][0] = "bes_termin";
                    requestData[0][1] = "Austragen";
                }
                else{
                    //Sing In
                    requestData[0][0] = "bes_termin";
                    requestData[0][1] = "Eintragen";
                }

                //Get Data from Backend
                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_signInTermin.php");
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
                    if(response.equals("2")){
                        Toast tNoConnection = Toast.makeText(getContext(), "Es haben sich bereits genügend Personen eingetragen.", Toast.LENGTH_LONG);
                        tNoConnection.show();
                    }
                    else{
                        updateFragment();
                    }
                }

            }
        });

        FloatingActionButton fabChangeTermin = (FloatingActionButton) rootView.findViewById(R.id.fab_change_termin);
        fabChangeTermin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open New Fragment with Input Fields
                Fragment024 mFragment024 = new Fragment024();
                mFragment024.setTermin(termin);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.container, mFragment024).addToBackStack("app").commit();
            }
        });
        FloatingActionButton fabDeleteTermin = (FloatingActionButton) rootView.findViewById(R.id.fab_delete_termin);
        fabDeleteTermin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.cancel();
                                //Set Request Data for Backend Connection
                                String[][] requestData = new String[2][2];
                                requestData[0][0] = "action";
                                requestData[0][1] = "del";
                                requestData[1][0] = "terId";
                                requestData[1][1] = termin.getId();

                                //Get Data from Backend
                                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_deleteTermin.php");
                                String response = "";
                                try {
                                    response = myRequester.execute().get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if(response.equals("")){
                                    Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
                                    tNoConnection.show();
                                }
                                else{
                                    if(response.equals("1")){
                                        Toast tNoConnection = Toast.makeText(getContext(), "Berechtigungsfehler", Toast.LENGTH_LONG);
                                        tNoConnection.show();
                                    }
                                    else{
                                        if(response.equals("ok")){
                                            goBack();
                                        }
                                        else{
                                            Toast tNoConnection = Toast.makeText(getContext(), "Fehler bei der Übertragung. Melden Sie sich bitte erneut an.", Toast.LENGTH_LONG);
                                            tNoConnection.show();
                                        }
                                    }
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Möchten Sie den Termin wirklich löschen?")
                        .setPositiveButton("Termin löschen", dialogClickListener)
                        .setNegativeButton("Abbrechen", dialogClickListener).show();
            }
        });
        FloatingActionButton fabAddPerson = (FloatingActionButton) rootView.findViewById(R.id.fab_add_person);
        //TODO: Prio3: Add Search Function
        fabAddPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_show_people_aviable);

                TextView tvTitle = (TextView) dialog.findViewById(R.id.TextView01);
                tvTitle.setText("Personal auswählen");


                String[] lv1_values = getAviablePeople(termin.getId());
                final ListView lv1 = (ListView) dialog.findViewById(R.id.listView1);
                lv1.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                final ArrayAdapter<String> lv1_adapter = new ArrayAdapter<String>(getActivity(), R.layout.listview_people_aviable_item, lv1_values);
                //ListAviablePeopleTerminAdapter lv1_adapter = new ListAviablePeopleTerminAdapter(getActivity(), aviablePeople);
                lv1.setAdapter(lv1_adapter);

                Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
                btnNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SparseBooleanArray checked = lv1.getCheckedItemPositions();
                        String response = "";
                        for (int i = 0; i < lv1.getCount(); i++) {
                            if (checked.get(i)) {

                                //Set Request Data for Backend Connection
                                String[][] requestData = new String[3][2];
                                requestData[1][0] = "mglId";
                                requestData[1][1] = aviablePeople.get(i).getId();
                                requestData[2][0] = "terId";
                                requestData[2][1] = termin.getId();
                                requestData[0][0] = "bes_termin";
                                requestData[0][1] = "Eintragen";

                                //Get Data from Backend
                                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_signInTermin.php");
                                try {
                                    response = myRequester.execute().get();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        if (response.equals("")) {
                            Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
                            tNoConnection.show();
                        } else {
                            if (response.equals("2")) {
                                Toast tNoConnection = Toast.makeText(getContext(), "Es haben sich bereits genügend Personen eingetragen", Toast.LENGTH_LONG);
                                tNoConnection.show();
                            }
                            else {
                                dialog.cancel();
                                updateFragment();
                            }
                        }
                    }
                });
                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                dialog.show();
            }
        });

        if(Profile.getRechteIds().contains("6")){
            fabAddPerson.setVisibility(View.VISIBLE);
            fabChangeTermin.setVisibility(View.VISIBLE);
            fabDeleteTermin.setVisibility(View.VISIBLE);
        }
        else{
            fabAddPerson.setVisibility(View.INVISIBLE);
            fabChangeTermin.setVisibility(View.INVISIBLE);
            fabDeleteTermin.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }

    public void updateFragment(){
        //Set Request Data for Backend Connection
        String[][] requestData = new String[1][2];
        requestData[0][0] = "terminId";
        requestData[0][1] = this.termin.getId();

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
        }else{
            Fragment023 mFragment023 = new Fragment023();
            mFragment023.setData(StringOperator.extractBetweenSpaces(response));
            mFragment023.buildDataModel();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.container, mFragment023).commit();
        }
    }

    public String[] getAviablePeople(String terId){
        //Set Request Data for Backend Connection
        String[][] requestData = new String[1][2];
        requestData[0][0] = "terId";
        requestData[0][1] = terId;

        //Get Data from Backend
        HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_getAviablePeople.php");
        String response = "";
        try{
            response = myRequester.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

        aviablePeople = new ArrayList<>();
        String[] responseData = StringOperator.extractBetweenSpaces(response);
        String[] rueck = new String[(responseData.length-1)/3];
        int counterInner = 0;
        for(int i = 1; i < responseData.length; i = i + 3){
            Mitglied mitglied = new Mitglied(responseData[i], responseData[i+1], responseData[i+2]); //Add to Data Model to find ID later
            rueck[counterInner] = responseData[i+1] + " " + responseData[i]; //Show Nachname Vorname
            counterInner++;
            aviablePeople.add(mitglied);
        }
        return rueck;
    }

    public void openDatePickerDialogBeg(final String personId){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_date);
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            datePicker.setMinDate(termin.getBeginn().getTimeInMillis());
            datePicker.setMaxDate(termin.getEnde().getTimeInMillis());
        }

        TextView tvTitle = (TextView) dialog.findViewById(R.id.TextView01);
        tvTitle.setText("Anwesenheitszeit ändern (Beginn)");

        TextView tvName = (TextView) dialog.findViewById(R.id.TextView02);
        tvName.setText(personId);

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aenderungBegZeit.set(Calendar.YEAR, datePicker.getYear());
                aenderungBegZeit.set(Calendar.MONTH, datePicker.getMonth());
                aenderungBegZeit.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                dialog.cancel();
                openTimePickerDialogBeg(personId);
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void openTimePickerDialogBeg(final String personId){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_time);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(termin.getBeginn().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(termin.getBeginn().get(Calendar.MINUTE));
        }
        else{
            timePicker.setCurrentHour(termin.getBeginn().get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(termin.getBeginn().get(Calendar.MINUTE));
        }

        TextView tv1 = (TextView) dialog.findViewById(R.id.TextView01);
        tv1.setText("Anwesenheitszeit ändern (Beginn)");

        TextView tv2 = (TextView) dialog.findViewById(R.id.TextView02);
        tv2.setText(personId);

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    aenderungBegZeit.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    aenderungBegZeit.set(Calendar.MINUTE, timePicker.getMinute());
                }
                else{
                    aenderungBegZeit.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    aenderungBegZeit.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                }
                if (aenderungBegZeit.get(Calendar.YEAR) == termin.getEnde().get(Calendar.YEAR) &&
                        aenderungBegZeit.get(Calendar.MONTH) == termin.getEnde().get(Calendar.MONTH) &&
                        aenderungBegZeit.get(Calendar.DAY_OF_MONTH) == termin.getEnde().get(Calendar.DAY_OF_MONTH)){

                    aenderungEndZeit.set(Calendar.YEAR, termin.getBeginn().get(Calendar.YEAR));
                    aenderungEndZeit.set(Calendar.MONTH, termin.getBeginn().get(Calendar.MONTH));
                    aenderungEndZeit.set(Calendar.DAY_OF_MONTH, termin.getBeginn().get(Calendar.DAY_OF_MONTH));

                    dialog.cancel();
                    openTimePickerDialogEnd(personId);
                }
                else{
                    dialog.cancel();
                    openDatePickerDialogEnd(personId);
                }

            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void openDatePickerDialogEnd(final String personId){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_date);
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN){
            datePicker.setMinDate(aenderungBegZeit.getTimeInMillis());
            datePicker.setMaxDate(termin.getEnde().getTimeInMillis());
        }

        TextView tvTitle = (TextView) dialog.findViewById(R.id.TextView01);
        tvTitle.setText("Anwesenheitszeit ändern (Ende)");

        TextView tvName = (TextView) dialog.findViewById(R.id.TextView02);
        tvName.setText(personId);

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aenderungEndZeit.set(Calendar.YEAR, datePicker.getYear());
                aenderungEndZeit.set(Calendar.MONTH, datePicker.getMonth());
                aenderungEndZeit.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                dialog.cancel();
                openTimePickerDialogEnd(personId);
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void openTimePickerDialogEnd(String personId){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_time);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        TextView tv1 = (TextView) dialog.findViewById(R.id.TextView01);
        tv1.setText("Anwesenheitszeit ändern (Ende)");

        TextView tv2 = (TextView) dialog.findViewById(R.id.TextView02);
        tv2.setText(personId);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(termin.getEnde().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(termin.getEnde().get(Calendar.MINUTE));
        }
        else{
            timePicker.setCurrentHour(termin.getEnde().get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(termin.getEnde().get(Calendar.MINUTE));
        }

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setText("Änderungen speichern");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    aenderungEndZeit.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    aenderungEndZeit.set(Calendar.MINUTE, timePicker.getMinute());
                }
                else{
                    aenderungEndZeit.set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    aenderungEndZeit.set(Calendar.MINUTE, timePicker.getCurrentMinute());
                }
                changeTimeRequest();
                dialog.cancel();
            }
        });

        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }

    public void changeTimeRequest(){
        //Set Request Data for Backend Connection
        String[][] requestData = new String[13][2];
        requestData[0][0] = "perId";
        requestData[0][1] = aenderungPerId;
        requestData[1][0] = "terId";
        requestData[1][1] = termin.getId();
        requestData[2][0] = "tmb_datum_von_tag";
        requestData[2][1] = String.format("%02d", aenderungBegZeit.get(Calendar.DAY_OF_MONTH));
        requestData[3][0] = "tmb_datum_von_monat";
        requestData[3][1] = String.format("%02d", aenderungBegZeit.get(Calendar.MONTH) + 1);
        requestData[4][0] = "tmb_datum_von_jahr";
        requestData[4][1] = "" + aenderungBegZeit.get(Calendar.YEAR);
        requestData[5][0] = "tmb_datum_bis_tag";
        requestData[5][1] = String.format("%02d", aenderungEndZeit.get(Calendar.DAY_OF_MONTH));
        requestData[6][0] = "tmb_datum_bis_monat";
        requestData[6][1] = String.format("%02d", aenderungEndZeit.get(Calendar.MONTH) + 1);
        requestData[7][0] = "tmb_datum_bis_jahr";
        requestData[7][1] = "" + aenderungEndZeit.get(Calendar.YEAR);
        requestData[8][0] = "tmb_uhrzeit_von_h";
        requestData[8][1] = String.format("%02d", aenderungBegZeit.get(Calendar.HOUR_OF_DAY));
        requestData[9][0] = "tmb_uhrzeit_von_m";
        requestData[9][1] = String.format("%02d", aenderungBegZeit.get(Calendar.MINUTE));
        requestData[10][0] = "tmb_uhrzeit_bis_h";
        requestData[10][1] = String.format("%02d", aenderungEndZeit.get(Calendar.HOUR_OF_DAY));
        requestData[11][0] = "tmb_uhrzeit_bis_m";
        requestData[11][1] = String.format("%02d", aenderungEndZeit.get(Calendar.MINUTE));
        requestData[12][0] = "aend_mitglied";
        requestData[12][1] = "aendern";


        //Get Data from Backend
        HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_changeTimeTermin.php?action=zeit");
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
            if(response.equals("1")){
                Toast tNoConnection = Toast.makeText(getContext(), "Eingabefehler: Die angegebenen Uhrzeiten müssen sich im Zeitrahmen des Termins befinden.", Toast.LENGTH_LONG);
                tNoConnection.show();
            }
            else{
                updateFragment();
            }
        }
    }

    public void goBack(){
        //Get Data from Backend
        HTTPRequester myRequester = new HTTPRequester(null, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_allTermine.php");
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
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Fragment022 mFragment022 = new Fragment022();
            mFragment022.setData(StringOperator.extractBetweenSpaces(response));
            mFragment022.buildDataModel();
            fragmentManager.beginTransaction().replace(R.id.container, mFragment022).addToBackStack("app").commit();
        }

    }

    //TODO: Save User Credentials
}
