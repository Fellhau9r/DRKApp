package com.example.d062434.drkapp.helper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.d062434.drkapp.Fragment02;
import com.example.d062434.drkapp.Fragment03;
import com.example.d062434.drkapp.R;
import com.example.d062434.drkapp.data.Profile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by D062434 on 27.10.2015.
 */
public class CustomCalendarNfhView extends TableLayout{

    public static int numberOfWeekDaysShown = 6;

    private Calendar calendar;
    private Fragment03 fragment03;
    private ArrayList<CustomCalendarDay> ccds = new ArrayList<>();

    public CustomCalendarNfhView(Context context) {
        super(context);
    }

    public CustomCalendarNfhView(Context context, Fragment03 fragment03){
        super(context);

        SharedPreferences prefs = fragment03.getActivity().getSharedPreferences("com.example.drkapp", Context.MODE_PRIVATE);
        if(Profile.getScreenDimension() > 0.5625){
            numberOfWeekDaysShown = 5;
        }

        calendar = Calendar.getInstance();
        this.fragment03 = fragment03;
        updateCalendar(context);

    }

    public CustomCalendarNfhView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public static String getCalendarMonth(boolean thisMonth){
        String rueck = "";
        Calendar tempCalendar = Calendar.getInstance();
        if(thisMonth == false){
            tempCalendar.add(Calendar.MONTH, 1);
        }
        rueck += tempCalendar.get(Calendar.YEAR);
        rueck += String.format("%02d", tempCalendar.get(Calendar.MONTH) + 1);
        return rueck;
    }

    public static String getEndOfWeek(Calendar newDate){
        String rueck = "";
        newDate.add(Calendar.DAY_OF_MONTH, numberOfWeekDaysShown);
        rueck += newDate.get(Calendar.YEAR);
        if(newDate.get(Calendar.MONTH) < 9){
            rueck += "0" + (newDate.get(Calendar.MONTH)+1);
        }
        else{
            rueck += (newDate.get(Calendar.MONTH)+1);
        }
        if(newDate.get(Calendar.DAY_OF_MONTH)< 10){
            rueck += "0" + newDate.get(Calendar.DAY_OF_MONTH);
        }
        else {
            rueck += newDate.get(Calendar.DAY_OF_MONTH);
        }
        return rueck;
    }


    public void updateCalendar(Context context){
        this.ccds = new ArrayList<>();
        this.removeAllViews();
        loadCalendarContent();
        initCalendar(context);
    }

    //Build all TextViews with corresponding Content
    public void initCalendar(final Context context){

        //Get Device Scalings
        float textSize = getResources().getDimension(R.dimen.nfh_calendar);
        int padding_in_dp = 2;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        llp.setMargins(padding_in_px, 0, padding_in_px, 0);

        TableRow.LayoutParams tlp = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tlp.setMargins(0, 0, padding_in_px, padding_in_px);

        //Initialize Table Header
        TableRow tr_head = new TableRow(context);
        TextView mitglied = new TextView(context);
        mitglied.setText("Mitglied");
        mitglied.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        mitglied.setLayoutParams(tlp);
        mitglied.setPadding(padding_in_px, 0, padding_in_px, 0);
        tr_head.addView(mitglied);

        //Fill TextViews with Dates (TableHead)
        for(int i = 0; i < numberOfWeekDaysShown; i++){
            TextView day = new TextView(context);
            day.setText(getWeekDay(i));
            tr_head.addView(day);
            day.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            day.setLayoutParams(tlp);
            day.setPadding(padding_in_px, 0, padding_in_px, 0);
        }
        this.addView(tr_head);

        //Fill vertical all the names of people which are a member of NFH
        for(int i = 0; i < fragment03.getAnzNFHMitglieder(); i++){
            final String NFH_MGL_ID = fragment03.getNFHMitglied(i).getId();
            final String NFH_MGL_NAME = fragment03.getNFHMitglied(i).getVorname() + " " + fragment03.getNFHMitglied(i).getNachname();

            TableRow tr_mitglied = new TableRow(context);
            TextView name = new TextView(context);
            name.setText(fragment03.getNFHMitglied(i).getVorname() + " " + fragment03.getNFHMitglied(i).getNachname());
            name.setLayoutParams(tlp);
            name.setPadding(padding_in_px, 0, padding_in_px, 0);
            name.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            name.setClickable(false);
            tr_mitglied.addView(name);

            //Find corresponding entries to these people
            for(int j = 0; j < numberOfWeekDaysShown; j++){
                final String DATE = getCurrentDate(j);

                CustomNFHTextView click = new CustomNFHTextView(context);
                click.setPosMitglied(i);
                click.setPosDay(j);
                click.setText("");
                click.setLayoutParams(tlp);
                click.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);

                Drawable background;
                final int hintergrundId = ermittleHintergrund(j, fragment03.getNFHMitglied(i).getId());
                switch(hintergrundId){
                    case 1: background = getAviableDrawable(); break;
                    case 2: background = getNotAviableDrawable(); break;
                    default: background = getBlankDrawable(click); break;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    click.setBackground(background);
                }
                else{
                    click.setBackgroundDrawable(background);
                }
                //Handler for pressing on TextView, Open NFH Dialog
                if(fragment03.getNFHMitglied(i).getId().equals(Profile.getId()) || Profile.getRechteIds().contains("14")){
                    click.setOnClickListener(new OnClickListener() {
                        private String mglId = NFH_MGL_ID;
                        private String mglName = NFH_MGL_NAME;
                        private String date = DATE;

                        @Override
                        public void onClick(View v) {
                            fragment03.launchNFHDialog(mglId, mglName, date, hintergrundId);
                        }
                    });
                }
                click.setClickable(true);
                tr_mitglied.addView(click);
            }
            this.addView(tr_mitglied);
        }

    }



    public void loadCalendarContent(){
        //Get Data of Request and build own Data Model for Handling the NFH Entries
        for(int i = 0; i < numberOfWeekDaysShown; i++){
            Calendar calendar = (Calendar) this.calendar.clone();
            calendar.add(Calendar.SECOND, 86400 * i);
            CustomCalendarDay ccd = new CustomCalendarDay(calendar);
            for(int j = 0; j < this.fragment03.getNfhEintraege().size(); j++){
                if(calendar.get(Calendar.YEAR) == this.fragment03.getNfhEintraege().get(j).getDatum().get(Calendar.YEAR) &&
                        calendar.get(Calendar.MONTH) == this.fragment03.getNfhEintraege().get(j).getDatum().get(Calendar.MONTH) &&
                        calendar.get(Calendar.DAY_OF_MONTH) == this.fragment03.getNfhEintraege().get(j).getDatum().get(Calendar.DAY_OF_MONTH)){
                    if(this.fragment03.getNfhEintraege().get(j).getStatus().equals("B")){
                        ccd.addPersonId(this.fragment03.getNfhEintraege().get(j).getMitgliedId());
                    }
                    else{
                        if(this.fragment03.getNfhEintraege().get(j).getStatus().equals("N")){
                            ccd.addAbsenceId(this.fragment03.getNfhEintraege().get(j).getMitgliedId());
                        }

                    }
                    this.ccds.add(ccd);
                }
            }
        }
    }


    //Get Day of Week as German String
    public String getWeekDay(int numberOfDays){
        String rueck = "";
        int dayMultiplikator = numberOfDays * 86400;
        Calendar tempCalendar = (Calendar) this.calendar.clone();
        tempCalendar.add(Calendar.SECOND, dayMultiplikator);
        int date = tempCalendar.get(Calendar.DAY_OF_WEEK);
        switch(date){
            case 1: rueck += "So.\n"; break;
            case 2: rueck += "Mo.\n"; break;
            case 3: rueck += "Di.\n"; break;
            case 4: rueck += "Mi.\n"; break;
            case 5: rueck += "Do.\n"; break;
            case 6: rueck += "Fr.\n"; break;
            case 7: rueck += "Sa.\n"; break;
        }
        if(tempCalendar.get(Calendar.DAY_OF_MONTH) < 10){
            String day = "0";
            rueck += day.concat("" + tempCalendar.get(Calendar.DAY_OF_MONTH));

        }
        else{
            rueck += tempCalendar.get(Calendar.DAY_OF_MONTH);
        }
        if(tempCalendar.get(Calendar.MONTH)+1 < 10){
            String month = ".0";
            rueck += month.concat("" + (tempCalendar.get(Calendar.MONTH) + 1));
        }
        else{
            rueck += "." + (tempCalendar.get(Calendar.MONTH) + 1);
        }
        return rueck;
    }

    //Get Name of the Month as German String
    public String getCurrentMonth(){
        String rueck = "";
        switch(this.calendar.get(Calendar.MONTH)){
            case 0: rueck += "Januar"; break;
            case 1: rueck += "Februar"; break;
            case 2: rueck += "März"; break;
            case 3: rueck += "April"; break;
            case 4: rueck += "Mai"; break;
            case 5: rueck += "Juni"; break;
            case 6: rueck += "Juli"; break;
            case 7: rueck += "August"; break;
            case 8: rueck += "September"; break;
            case 9: rueck += "Oktober"; break;
            case 10: rueck += "November"; break;
            case 11: rueck += "Dezember"; break;
        }
        return rueck + " " + this.calendar.get(Calendar.YEAR);
    }

    //Get the Dates in relation to the dayCounter which counts forward
    public String getCurrentDate(int dayCounter){
        String rueck = "";
        Calendar tempCalendar = (Calendar) this.calendar.clone();
        tempCalendar.add(Calendar.SECOND, 86400*dayCounter);
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
        return rueck;
    }

    //Handler for pressing Button Next Week
    public void showNextWeek(Context context){
        this.calendar.add(Calendar.SECOND, 86400*this.numberOfWeekDaysShown);
        fragment03.sendRequest(this.calendar);
        updateCalendar(context);
    }

    //Handler for pressing Button Previous Week
    public void showPrevWeek(Context context){
        this.calendar.add(Calendar.SECOND, -86400*this.numberOfWeekDaysShown);
        fragment03.sendRequest(this.calendar);
        updateCalendar(context);
    }

    //Find the right background in relation to the NFH Entries.
    //Possible are:
    // Aviable Screen - returns 1
    // Not Aviable Screen - returns 2
    // No Entrie - returns 0
    public int ermittleHintergrund(int dayNumber, String person){
        Calendar tempCalendar = (Calendar) this.calendar.clone();
        tempCalendar.add(Calendar.SECOND, dayNumber * 86400);

        int currentDay = tempCalendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = tempCalendar.get(Calendar.MONTH);
        int currentYear = tempCalendar.get(Calendar.YEAR);

        for(int i = 0; i < this.ccds.size(); i++){
            if(currentYear == ccds.get(i).getYear() &&
                    currentMonth == ccds.get(i).getMonth() &&
                    currentDay == ccds.get(i).getDay()){
                for(int j = 0; j < ccds.get(i).getPeopleIds().size(); j++){
                    if(person.equals(ccds.get(i).getPeopleIds().get(j))){
                        return 1;
                    }
                }
                for(int j = 0; j < ccds.get(i).getAbsenceIds().size(); j++){
                    if(person.equals(ccds.get(i).getAbsenceIds().get(j))){
                        return 2;
                    }
                }
                return 0;
            }
        }
        return 0;
    }

    //Background Drawable for No Entry Screen (Code 0)
    public Drawable getBlankDrawable(final TextView textView){
        Drawable rueck = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Paint paint = new Paint();
                paint.setColor(getResources().getColor(R.color.colorPrimary));
                canvas.drawRect(0, 0, 150, 150, paint);

                int border_stroke_dp = 1;  // 6 dps
                final float scale = getResources().getDisplayMetrics().density;
                int border_stroke_px = (int) (border_stroke_dp * scale + 0.5f);

                paint.setColor(getResources().getColor(R.color.colorPrimaryLight));
                canvas.drawRect(border_stroke_px, border_stroke_px, textView.getWidth() - border_stroke_px, textView.getHeight() - border_stroke_px, paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        return rueck;
    }

    //Background Drawable for Aviable Screen (Code 1)
    public Drawable getAviableDrawable(){
        Drawable rueck = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Paint paint = new Paint();
                paint.setColor(getResources().getColor(R.color.colorAccent));
                canvas.drawRect(0,0,150,150, paint);
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };
        return rueck;
    }

    //Background Drawable for Not Aviable Screen (Code 2)
    public Drawable getNotAviableDrawable(){

        Drawable rueck = new Drawable() {
            @Override
            public void draw(Canvas canvas) {
                Paint paint = new Paint();
                paint.setColor(Color.LTGRAY);
                paint.setStrokeWidth(5);
                for(int i = 0; i < 10; i++){
                    int posAnfangY = 60 - i * 20;
                    int posEndeY = 60;
                    int posAnfangX = 0;
                    int posEndeX = i * 20;
                    canvas.drawLine(posAnfangX, posAnfangY, posEndeX, posEndeY, paint);
                }
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return 0;
            }
        };

        return rueck;
    }

    //Class for Handling each Day of the week
    private class CustomCalendarDay{
        private int day;
        private int month;
        private int year;
        private ArrayList<String> peopleIds = new ArrayList<>(); //All People which are aviable
        private ArrayList<String> absenceIds = new ArrayList<>(); //All People which are NOT aviable

        public CustomCalendarDay(Calendar calendar){
            this.day = calendar.get(Calendar.DAY_OF_MONTH);
            this.month = calendar.get(Calendar.MONTH);
            this.year = calendar.get(Calendar.YEAR);
        }

        public void addPersonId(String person){
            this.peopleIds.add(person);
        }

        public void addAbsenceId(String person){
            this.absenceIds.add(person);
        }

        public int getYear(){
            return this.year;
        }

        public int getMonth(){
            return this.month;
        }

        public int getDay(){
            return this.day;
        }

        public ArrayList<String> getPeopleIds(){
            return peopleIds;
        }

        public ArrayList<String> getAbsenceIds(){
            return absenceIds;
        }
    }

}
