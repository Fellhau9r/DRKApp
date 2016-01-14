package com.example.d062434.drkapp;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.d062434.drkapp.data.Mitglied;
import com.example.d062434.drkapp.data.Profile;
import com.example.d062434.drkapp.data.Termin;
import com.example.d062434.drkapp.data.Terminkategorie;
import com.example.d062434.drkapp.helper.CustomPersonalView;
import com.example.d062434.drkapp.helper.HTTPRequester;
import com.example.d062434.drkapp.helper.ListPersonTerminAdapter;
import com.example.d062434.drkapp.helper.StringOperator;

import java.util.Calendar;

/**
 * Created by D062434 on 25.10.2015.
 */
public class Fragment025 extends Fragment{
    private Termin termin;
    private Termin aenderungTermin = new Termin();
    private String aenderungMinPers;
    private String aenderungMaxPers;

    public Fragment025() {
        super();

    }

    public void setTermin(Termin newTermin){
        this.termin = newTermin;
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment024, container, false);
        aenderungTermin = termin;
        aenderungMinPers = "" + aenderungTermin.getMinPers();
        aenderungMaxPers = "" + aenderungTermin.getMaxPers();

        // Set Table Content
        final EditText tv1 = (EditText) rootView.findViewById(R.id.TextView01);
        Spinner tv2 = (Spinner) rootView.findViewById(R.id.textView4);
        final EditText tv3 = (EditText) rootView.findViewById(R.id.textView8);
        final EditText tv4 = (EditText) rootView.findViewById(R.id.textView10);
        TextView tv5 = (TextView) rootView.findViewById(R.id.textView12);
        TextView tv6 = (TextView) rootView.findViewById(R.id.textView14);
        Switch tv7 = (Switch) rootView.findViewById(R.id.textView16);
        final TextView tv8 = (TextView) rootView.findViewById(R.id.textView19);
        final EditText tv9 = (EditText) rootView.findViewById(R.id.editText21);
        final EditText tv10 = (EditText) rootView.findViewById(R.id.editText23);
        final TextView tv11 = (TextView) rootView.findViewById(R.id.textView25);
        final Switch tv12 = (Switch) rootView.findViewById(R.id.switch2);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.terminbesetzung_array, R.layout.listview_spinner_terminkategorie);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tv2.setAdapter(adapter);

        for(int i = 0; i < adapter.getCount(); i++){
            if(adapter.getItem(i).equals(termin.getKategorie())){
                tv2.setSelection(i);
            }
        }
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
            tv8.setText("Ja");
            tv7.setChecked(true);
        }
        else{
            tv8.setText("Nein");
            tv7.setChecked(false);
        }

        tv9.setText("" + termin.getMinPers());
        tv10.setText("" + termin.getMaxPers());

        if(termin.isPersUnbegrenzt()){
            tv11.setText("Ja");
            tv12.setChecked(true);
        }
        else{
            tv11.setText("Nein");
            tv12.setChecked(false);
        }

        tv2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        aenderungTermin.setKategorie(Terminkategorie.AUSBILDUNG);
                        break;
                    case 1:
                        aenderungTermin.setKategorie(Terminkategorie.BEREITSCHAFT);
                        break;
                    case 2:
                        aenderungTermin.setKategorie(Terminkategorie.BESPRECHUNG);
                        break;
                    case 3:
                        aenderungTermin.setKategorie(Terminkategorie.BLUTSPENDEN);
                        break;
                    case 4:
                        aenderungTermin.setKategorie(Terminkategorie.FESTVERANSTALTUNG);
                        break;
                    case 5:
                        aenderungTermin.setKategorie(Terminkategorie.LEHRGANG);
                        break;
                    case 6:
                        aenderungTermin.setKategorie(Terminkategorie.SANDIENST);
                        break;
                    case 7:
                        aenderungTermin.setKategorie(Terminkategorie.TURNIERTEILNAHME);
                        break;
                    case 8:
                        aenderungTermin.setKategorie(Terminkategorie.UEBUNGSEINHEIT);
                        break;
                    default:
                        aenderungTermin.setKategorie(Terminkategorie.SONSTIGES);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        tv1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aenderungTermin.setBezeichnung("" + tv1.getText());
            }
        });

        tv3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aenderungTermin.setBeschreibung("" + tv3.getText());
            }
        });

        tv4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aenderungTermin.setOrt("" + tv4.getText());
            }
        });

        tv9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aenderungMinPers = "" + tv9.getText();
            }
        });

        tv10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                aenderungMaxPers = "" + tv10.getText();
            }
        });

        tv7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv8.setText("Ja");
                    aenderungTermin.setStatistikrelevanz(true);
                } else {
                    tv8.setText("Nein");
                    aenderungTermin.setStatistikrelevanz(false);
                }
            }
        });

        tv12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv11.setText("Ja");
                    aenderungTermin.setPersUnbegrenzt(true);
                } else {
                    tv11.setText("Nein");
                    aenderungTermin.setPersUnbegrenzt(false);
                }
            }
        });

        Button btnCancel = (Button) rootView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               goBack();
            }
        });

        Button btnSave = (Button) rootView.findViewById(R.id.btnSaveChanges);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv1.getText().equals("")){
                    tv1.setError("Titel wird benötigt!");
                }else{
                    if(tv3.getText().equals("")){
                        tv3.setError("Beschreibung wird benötigt!");
                    }else{
                        if(tv4.getText().equals("")){
                            tv4.setError("Ort wird benötigt!");
                        }
                        else{
                            if(tv9.getText().equals("")){
                                tv9.setError("Mind. Teilnehmerzahl wird benötigt!");
                            }
                            else{
                                aenderungTermin.setMinPers(Integer.parseInt(aenderungMinPers));
                                if(tv10.getText().equals("") && tv12.isChecked() == false){
                                    tv10.setError("Max. Teilnehmerzahl wird benötigt!");
                                }
                                else{
                                    aenderungTermin.setMaxPers(Integer.parseInt(aenderungMaxPers));
                                    if(tv10.getText().equals("0") && tv12.isChecked() == false){
                                        tv10.setError("Max. Teilnehmerzahl darf nicht 0 sein!");
                                    }
                                    else{
                                        if(aenderungTermin.getMaxPers() < aenderungTermin.getMinPers() && tv12.isChecked() == false){
                                            tv10.setError("Max. Teilnehmerzahl darf nicht niedriger als die Min. Teilnehmerzahl sein!");
                                        }
                                        else{
                                            addNewTerminRequest();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

        TableRow trDate = (TableRow) rootView.findViewById(R.id.tablerow_date);
        TableRow trTime = (TableRow) rootView.findViewById(R.id.tablerow_time);
        trDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialogBeg();
            }
        });

        trTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTimePickerDialogBeg();
            }
        });

        return rootView;
    }

    public void updateFragment(){

        Fragment025 mFragment025 = new Fragment025();
        mFragment025.setTermin(this.aenderungTermin);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        fragmentManager.beginTransaction().replace(R.id.container, mFragment025).addToBackStack("app").commit();
    }

    public void openDatePickerDialogBeg(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_date);
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            datePicker.setMinDate(Calendar.getInstance().getTimeInMillis());
        }
        TextView tvTitle = (TextView) dialog.findViewById(R.id.TextView01);
        tvTitle.setText("Datum ändern (Beginn)");

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aenderungTermin.getBeginn().set(Calendar.YEAR, datePicker.getYear());
                aenderungTermin.getBeginn().set(Calendar.MONTH, datePicker.getMonth());
                aenderungTermin.getBeginn().set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                dialog.cancel();
                openDatePickerDialogEnd();
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

    public void openTimePickerDialogBeg(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_time);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(termin.getBeginn().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(termin.getBeginn().get(Calendar.MINUTE));
        }

        TextView tv1 = (TextView) dialog.findViewById(R.id.TextView01);
        tv1.setText("Zeit ändern (Beginn)");

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    aenderungTermin.getBeginn().set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    aenderungTermin.getBeginn().set(Calendar.MINUTE, timePicker.getMinute());
                }
                else{
                    aenderungTermin.getBeginn().set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    aenderungTermin.getBeginn().set(Calendar.MINUTE, timePicker.getCurrentMinute());
                }
                dialog.cancel();
                openTimePickerDialogEnd();
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

    public void openDatePickerDialogEnd(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_date);
        final DatePicker datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            datePicker.setMinDate(aenderungTermin.getBeginn().getTimeInMillis());
        }

        TextView tvTitle = (TextView) dialog.findViewById(R.id.TextView01);
        tvTitle.setText("Datum ändern (Ende)");

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aenderungTermin.getEnde().set(Calendar.YEAR, datePicker.getYear());
                aenderungTermin.getEnde().set(Calendar.MONTH, datePicker.getMonth());
                aenderungTermin.getEnde().set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
                dialog.cancel();
                updateFragment();
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

    public void openTimePickerDialogEnd(){
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_change_time);
        final TimePicker timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(termin.getEnde().get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(termin.getEnde().get(Calendar.MINUTE));
        }

        TextView tvTitle = (TextView) dialog.findViewById(R.id.TextView01);
        tvTitle.setText("Zeit ändern (Ende)");

        Button btnNext = (Button) dialog.findViewById(R.id.btnNext);
        btnNext.setText("Termin speichern");
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    aenderungTermin.getEnde().set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    aenderungTermin.getEnde().set(Calendar.MINUTE, timePicker.getMinute());
                }
                else{
                    aenderungTermin.getEnde().set(Calendar.HOUR_OF_DAY, timePicker.getCurrentHour());
                    aenderungTermin.getEnde().set(Calendar.MINUTE, timePicker.getCurrentMinute());
                }
                dialog.cancel();
                updateFragment();
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

    public void addNewTerminRequest(){

        //Set Request Data for Backend Connection
        String[][] requestData = new String[19][2];
        requestData[0][0] = "ter_ver_name";
        requestData[0][1] = aenderungTermin.getBezeichnung();
        requestData[1][0] = "ter_ver_ort";
        requestData[1][1] = aenderungTermin.getOrt();
        requestData[2][0] = "ter_stat_aktiv";
        requestData[2][1] = "J";
        requestData[3][0] = "ter_beschreibung";
        requestData[3][1] = aenderungTermin.getBeschreibung();
        requestData[4][0] = "ter_per_min";
        requestData[4][1] = "" + aenderungTermin.getMinPers();
        requestData[5][0] = "ter_per_max";
        requestData[5][1] = "" + aenderungTermin.getMaxPers();
        requestData[6][0] = "ter_per_max_unb";

        if(aenderungTermin.isPersUnbegrenzt()){
            requestData[6][1] = "J";
        }else{
            requestData[6][1] = "";
        }

        requestData[7][0] = "ter_kategorie";
        requestData[7][1] = aenderungTermin.getKategorie();
        requestData[8][0] = "ter_dat_von_tag";
        requestData[8][1] = "" + aenderungTermin.getBeginn().get(Calendar.DAY_OF_MONTH);
        requestData[9][0] = "ter_dat_von_monat";
        requestData[9][1] = "" + (aenderungTermin.getBeginn().get(Calendar.MONTH) + 1);
        requestData[10][0] = "ter_dat_von_jahr";
        requestData[10][1] = "" + aenderungTermin.getBeginn().get(Calendar.YEAR);
        requestData[11][0] = "ter_dat_bis_tag";
        requestData[11][1] = "" + aenderungTermin.getEnde().get(Calendar.DAY_OF_MONTH);
        requestData[12][0] = "ter_dat_bis_monat";
        requestData[12][1] = "" + (aenderungTermin.getEnde().get(Calendar.MONTH) + 1);
        requestData[13][0] = "ter_dat_bis_jahr";
        requestData[13][1] = "" + aenderungTermin.getEnde().get(Calendar.YEAR);
        requestData[14][0] = "ter_uhrzeit_von_std";
        requestData[14][1] = "" + aenderungTermin.getBeginn().get(Calendar.HOUR_OF_DAY);
        requestData[15][0] = "ter_uhrzeit_von_min";
        requestData[15][1] = "" + aenderungTermin.getBeginn().get(Calendar.MINUTE);
        requestData[16][0] = "ter_uhrzeit_bis_std";
        requestData[16][1] = "" + aenderungTermin.getEnde().get(Calendar.HOUR_OF_DAY);
        requestData[17][0] = "ter_uhrzeit_bis_min";
        requestData[17][1] = "" + aenderungTermin.getEnde().get(Calendar.MINUTE);
        requestData[18][0] = "add_termin";
        requestData[18][1] = "add";

        //Get Data from Backend
        HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_addTermin.php");
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
            if(response.equals("ok")){
                goBack();
            }
            else{
                Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
                tNoConnection.show();
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
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().replace(R.id.container, mFragment022).addToBackStack("app").commit();
        }
    }
}
