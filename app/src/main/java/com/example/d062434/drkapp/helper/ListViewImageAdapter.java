package com.example.d062434.drkapp.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.d062434.drkapp.R;

/**
 * Created by D062434 on 19.10.2015.
 */
public class ListViewImageAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] itemname;
    private final String[] itemdate;
    private final int[] imgid;

    public ListViewImageAdapter(Activity context, String[] itemname, int[] imgid, String[] itemdate) {
        super(context, R.layout.listview_fragment01_overview, itemname);

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
        this.itemdate = itemdate;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_fragment01_overview, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.listview_fragment01_overview_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listview_fragment01_overview_image);
        TextView txtDate = (TextView) rowView.findViewById(R.id.listview_fragment01_overview_date);

        txtTitle.setText(itemname[position]);
        imageView.setImageResource(imgid[position]);

        txtDate.setText(itemdate[position]);
        return rowView;

    };
}
