package com.example.d062434.drkapp.helper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.d062434.drkapp.R;
import com.example.d062434.drkapp.data.Mitglied;

import java.util.ArrayList;

/**
 * Created by D062434 on 19.10.2015.
 */
public class ListAviablePeopleTerminAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final ArrayList<Mitglied> mitglieder;

    public ListAviablePeopleTerminAdapter(Activity context, ArrayList<Mitglied> mitglieder) {
        super(context, R.layout.listview_people_aviable_item);

        this.context=context;
        this.mitglieder = mitglieder;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_people_aviable_item, null,true);

        CheckedTextView txtTitle = (CheckedTextView) rowView.findViewById(R.id.checkview1);
        txtTitle.setText(mitglieder.get(position).getNachname() + " " + mitglieder.get(position).getVorname());

        return rowView;

    };


}
