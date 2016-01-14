package com.example.d062434.drkapp;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Switch;
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
import com.example.d062434.drkapp.helper.Utility;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by D062434 on 25.10.2015.
 */
public class Fragment99 extends Fragment{

    public Fragment99() {
        super();

    }


    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment99, container, false);

        TextView userMail = (TextView) rootView.findViewById(R.id.textView9);
        TextView userPassword = (TextView) rootView.findViewById(R.id.textView11);
        final TextView tvNot1 = (TextView) rootView.findViewById(R.id.textView03);
        final TextView tvNot2 = (TextView) rootView.findViewById(R.id.textView05);
        final TextView tvNot3 = (TextView) rootView.findViewById(R.id.textView07);
        Switch swNot1 = (Switch) rootView.findViewById(R.id.switch1);
        Switch swNot2 = (Switch) rootView.findViewById(R.id.switch2);
        Switch swNot3 = (Switch) rootView.findViewById(R.id.switch3);

        final SharedPreferences prefs = getActivity().getSharedPreferences("com.example.drkapp", Context.MODE_PRIVATE);

        if(prefs.getString("com.example.drkapp.not1Key", "true").equals("true")){
            tvNot1.setText("Aktiv");
            swNot1.setChecked(true);
        }
        else{
            tvNot1.setText("Inaktiv");
            swNot1.setChecked(false);
        }

        if(prefs.getString("com.example.drkapp.not2Key", "true").equals("true")){
            tvNot2.setText("Aktiv");
            swNot2.setChecked(true);
        }
        else{
            tvNot2.setText("Inaktiv");
            swNot2.setChecked(false);
        }

        if(prefs.getString("com.example.drkapp.not3Key", "true").equals("true")){
            tvNot3.setText("Aktiv");
            swNot3.setChecked(true);
        }
        else{
            tvNot3.setText("Inaktiv");
            swNot3.setChecked(false);
        }

        swNot1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tvNot1.setText("Aktiv");
                    prefs.edit().putString("com.example.drkapp.not1Key", "true").apply();
                }
                else{
                    tvNot1.setText("Inaktiv");
                    prefs.edit().putString("com.example.drkapp.not1Key", "false").apply();
                }
            }
        });

        swNot2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tvNot2.setText("Aktiv");
                    prefs.edit().putString("com.example.drkapp.not2Key", "true").apply();
                }
                else{
                    tvNot2.setText("Inaktiv");
                    prefs.edit().putString("com.example.drkapp.not2Key", "false").apply();
                }
            }
        });

        swNot3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    tvNot3.setText("Aktiv");
                    prefs.edit().putString("com.example.drkapp.not3Key", "true").apply();
                }
                else{
                    tvNot3.setText("Inaktiv");
                    prefs.edit().putString("com.example.drkapp.not3Key", "false").apply();
                }
            }
        });


        return rootView;
    }

}
