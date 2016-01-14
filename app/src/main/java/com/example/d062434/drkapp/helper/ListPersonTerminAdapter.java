package com.example.d062434.drkapp.helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.d062434.drkapp.R;
import com.example.d062434.drkapp.data.Profile;
import com.example.d062434.drkapp.data.Termin;

import org.w3c.dom.Text;

import java.util.Calendar;

/**
 * Created by D062434 on 19.10.2015.
 */
public class ListPersonTerminAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] itemname;
    private final String[] itemdate;

    public ListPersonTerminAdapter(Activity context, String[] itemname, String[] itemdate) {
        super(context, R.layout.listview_fragment01_overview, itemname);

        this.context=context;
        this.itemname=itemname;
        this.itemdate = itemdate;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_change_time, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.listview_change_time_text);
        TextView txtDate = (TextView) rowView.findViewById(R.id.listview_change_time_date);

        txtTitle.setText(itemname[position]);
        txtDate.setText(itemdate[position]);
        return rowView;

    };


}
