package com.example.d062434.drkapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d062434.drkapp.data.Profile;
import com.example.d062434.drkapp.data.Termin;
import com.example.d062434.drkapp.data.Terminkategorie;
import com.example.d062434.drkapp.data.Terminteilnahme;
import com.example.d062434.drkapp.helper.CustomCalendarNfhView;
import com.example.d062434.drkapp.helper.HTTPRequester;
import com.example.d062434.drkapp.helper.ListViewImageAdapter;
import com.example.d062434.drkapp.helper.StringOperator;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by D062434 on 25.10.2015.
 */
public class Fragment022 extends Fragment{

    private Fragment023 mFragment023 = new Fragment023();

    private String[] data;
    private ArrayList<Termin> termine = new ArrayList<>();


    public Fragment022() {
        super();

    }

    public void setData(String[] data){
        this.data = data;
    }

    public void buildDataModel(){
        termine = new ArrayList<>();

        int counterGlobal = 1;
        int counterInner = 0;
        //Setting User Termine
        String terminName = "";
        String terminDatum ="";
        String terminID = "";
        int terminBesatzung = 0;
        int terminMinPers = 0;
        int terminMaxPers = 0;

        while (counterGlobal < data.length){
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
    }

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment022, container, false);

        ListView lv1 = (ListView) rootView.findViewById(R.id.listView1);
        String lv1_values1[] = new String[this.termine.size()];
        String lv1_values3[] = new String[this.termine.size()];
        for(int i = 0; i < lv1_values1.length; i++){
            lv1_values1[i] = this.termine.get(i).getBezeichnung();
            lv1_values3[i] = this.termine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) + "." +
                    (this.termine.get(i).getBeginn().get(Calendar.MONTH)+1) + "." +
                    this.termine.get(i).getBeginn().get(Calendar.YEAR);
            if(this.termine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) != this.termine.get(i).getEnde().get(Calendar.DAY_OF_MONTH) ||
                    this.termine.get(i).getBeginn().get(Calendar.MONTH) != this.termine.get(i).getEnde().get(Calendar.MONTH) ||
                    this.termine.get(i).getBeginn().get(Calendar.YEAR) != this.termine.get(i).getEnde().get(Calendar.YEAR)){
                lv1_values3[i] += " - " + this.termine.get(i).getEnde().get(Calendar.DAY_OF_MONTH) + "." +
                        (this.termine.get(i).getEnde().get(Calendar.MONTH)+1) + "." +
                        this.termine.get(i).getEnde().get(Calendar.YEAR);
            }
        }
        int lv1_values2[] = new int[this.termine.size()];
        for(int i = 0; i < lv1_values2.length; i++){
            if(this.termine.get(i).getTeilnahme() == Terminteilnahme.NIEMAND){
                lv1_values2[i] = R.drawable.ter_rot;
            }
            else{
                if(this.termine.get(i).getTeilnahme() == Terminteilnahme.WENIGE){
                    lv1_values2[i] = R.drawable.ter_gelb;
                }
                else{
                    lv1_values2[i] = R.drawable.ter_gruen;
                }
            }
        }
        ListViewImageAdapter lv1_adapter = new ListViewImageAdapter(getActivity(), lv1_values1, lv1_values2, lv1_values3);
        lv1.setAdapter(lv1_adapter);

        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Set Request Data for Backend Connection
                String[][] requestData = new String[1][2];
                requestData[0][0] = "terminId";
                requestData[0][1] = termine.get(position).getId();

                //Get Data from Backend
                HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_termin.php");
                String response = "";
                try {
                    response = myRequester.execute().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.equals("")) {
                    Toast tNoConnection = Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG);
                    tNoConnection.show();
                } else {
                    mFragment023.setData(StringOperator.extractBetweenSpaces(response));
                    mFragment023.buildDataModel();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.container, mFragment023).addToBackStack("app").commit();
                }
            }
        });

        FloatingActionButton fabChangeTermin = (FloatingActionButton) rootView.findViewById(R.id.fab_add_termin);
        fabChangeTermin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open New Fragment with Input Fields
                Fragment025 mFragment025 = new Fragment025();
                mFragment025.setTermin(new Termin("", Terminkategorie.AUSBILDUNG, "", "", true, "", Calendar.getInstance(), Calendar.getInstance(), 0, 0, false));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction().replace(R.id.container, mFragment025).addToBackStack("app").commit();
            }
        });

        return rootView;
    }

    public void updateFragment(){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        //Get Data from Backend
        HTTPRequester myRequester = new HTTPRequester(null, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_allTermine.php");
        String response = "";
        try{
            response = myRequester.execute().get();
        }catch (Exception e){
            e.printStackTrace();
        }

        this.setData(StringOperator.extractBetweenSpaces(response));
        this.buildDataModel();
        fragmentManager.beginTransaction().replace(R.id.container, this).addToBackStack("app").commit();
    }
}
