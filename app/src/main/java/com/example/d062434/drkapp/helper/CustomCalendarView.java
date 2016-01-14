package com.example.d062434.drkapp.helper;

/**
 * Created by D062434 on 21.10.2015.
 */
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.example.d062434.drkapp.Fragment02;
import com.example.d062434.drkapp.Fragment023;
import com.example.d062434.drkapp.R;
import com.example.d062434.drkapp.data.Termin;
import com.example.d062434.drkapp.data.Terminteilnahme;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomCalendarView extends LinearLayout{
    private static final int CALENDAR_CELL_COLUMNS = 7;
    private static final int CALENDAR_CELL_ROWS = 6;
    // style
    private static final boolean DEFAULT_SHOW_WEEKLABEL = true;
    private static int DEFAULT_BG_WEEKLABEL;
    private static int DEFAULT_CELL_TEXTSIZE = 12;
    private static int DEFAULT_VALID_CELL_TEXTCOLOR;
    private static int DEFAULT_INVALID_CELL_TEXTCOLOR;
    private static int DEFAULT_BG_COLOR_TODAY;
    private static int DEFAULT_BG_COLOR_NORMAL_CELL;
    private static int DEFAULT_BG_COLOR_SELECTED_CELL;
    private static int DEFAULT_TODAY_TEXT_COLOR;
    private static int DEFAULT_SELECTED_TEXT_COLOR;
    //
    private static final boolean DEFAULT_SHOW_LINES = true;
    private static int DEFAULT_LINE_WIDTH = 1;
    private static int DEFAULT_LINE_COLOR;

    private Calendar currentCalendar;
    private Calendar maxCalendar;
    private Calendar minCalendar;

    private WeekLabel mWeekLabel;
    private GridView cellsGridView;


    private String[] mDays;
    private int firstDayPos;
    private int todayPos;
    private int lastDayPos;
    /**
     * custom attributes
     *
     *
     * @isShowWeekLabel
     * @bgColorOfWeek
     *
     * @invalidCellTextColor
     *
     * @bgOfNormalCell
     * @cellTextSize
     * @validCellTextColor
     * @bgOfSelectedCell
     * @bgColorOfToday
     *
     * @isShowLines
     * @lineWidth
     * @lineColor
     */

    // weekLabel attrs
    private boolean isShowWeekLabel;
    private int bgColorOfWeek;
    // invalidCell attrs
    private int invalidCellTextColor;
    //
    private float cellTextSize = DEFAULT_CELL_TEXTSIZE;
    private int validCellTextColor;
    private int todayTextColor;
    private int selectedTextColor;
    private int bgOfNormalCell;
    private int bgOfSelectedCell;
    private int bgColorOfToday;
    //
    private boolean isShowLines = DEFAULT_SHOW_LINES;
    private int lineColor;
    private int lineWidth = DEFAULT_LINE_WIDTH;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;

    public OnDateSelectedListener mOnDateSelectedListener;

    private ArrayList<Termin> termine = new ArrayList<>();
    private Fragment023 mFragment023 = new Fragment023(); //Open Detail Screen of Termin
    private Fragment02 mFragment02; //Parent Fragment, Termin Overview


    /**
     * OnDateChangeListener
     *
     * @author swordbearer
     *
     */
    public interface OnDateSelectedListener {
        public void onDateSelected(Calendar calendar);
    }

    public void setOnDateSelectedListener(OnDateSelectedListener listener) {
        this.mOnDateSelectedListener = listener;
    }

    public CustomCalendarView(Context context) {
        this(context, null);
    }

    public CustomCalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.calendar_view);
        this.setOrientation(LinearLayout.VERTICAL);


        initDefaultStyles();
        // week label
        isShowWeekLabel = array
                .getBoolean(R.styleable.calendar_view_showWeekLabel,
                        DEFAULT_SHOW_WEEKLABEL);
        bgColorOfWeek = array.getColor(
                R.styleable.calendar_view_bgColorOfWeekLabel,
                DEFAULT_BG_WEEKLABEL);
        // invalid cell
        invalidCellTextColor = array.getColor(
                R.styleable.calendar_view_invalidCellTextColor,
                DEFAULT_INVALID_CELL_TEXTCOLOR);
        // valid cell
        cellTextSize = array.getDimension(
                R.styleable.calendar_view_cellTextSize, DEFAULT_CELL_TEXTSIZE);
        validCellTextColor = array.getColor(
                R.styleable.calendar_view_validCellTextColor,
                DEFAULT_VALID_CELL_TEXTCOLOR);
        //
        bgOfNormalCell = array.getColor(
                R.styleable.calendar_view_bgColorOfNormalCell,
                DEFAULT_BG_COLOR_NORMAL_CELL);
        bgColorOfToday = array.getColor(
                R.styleable.calendar_view_bgColorOfToday,
                DEFAULT_BG_COLOR_TODAY);
        bgOfSelectedCell = array.getColor(
                R.styleable.calendar_view_bgColorOfSelectedCell,
                DEFAULT_BG_COLOR_SELECTED_CELL);
        //
        isShowLines = array.getBoolean(R.styleable.calendar_view_showLines,
                DEFAULT_SHOW_LINES);
        lineWidth = (int) array.getDimension(
                R.styleable.calendar_view_lineWidth, DEFAULT_LINE_WIDTH);
        lineColor = array.getColor(R.styleable.calendar_view_lineColor,
                DEFAULT_LINE_COLOR);
        if (currentCalendar == null) {
            currentCalendar = Calendar.getInstance();
        }
        initWeekLable(context);
        initCellGridView(context);
    }

    public void setFragment(Fragment023 mFragment023){
        this.mFragment023 = mFragment023;
    }


    private final void initDefaultStyles() {
        Resources res = getResources();
        DEFAULT_VALID_CELL_TEXTCOLOR = res
                .getColor(R.color.default_valid_cell_text_color);
        DEFAULT_INVALID_CELL_TEXTCOLOR = res
                .getColor(R.color.default_invalid_cell_text_color);
        DEFAULT_INVALID_CELL_TEXTCOLOR = res
                .getColor(R.color.default_invalid_cell_text_color);
        DEFAULT_BG_COLOR_TODAY = res.getColor(R.color.default_bg_today);
        DEFAULT_BG_COLOR_NORMAL_CELL = res
                .getColor(R.color.default_bg_normal_cell);
        DEFAULT_BG_COLOR_SELECTED_CELL = res
                .getColor(R.color.default_bg_selected_cell);
        DEFAULT_BG_WEEKLABEL = res.getColor(R.color.default_bg_weeklabel);
        DEFAULT_LINE_COLOR = res.getColor(R.color.default_line_color);
        DEFAULT_TODAY_TEXT_COLOR = res.getColor(R.color.colorPrimaryDark);
        DEFAULT_SELECTED_TEXT_COLOR = res.getColor(R.color.colorPrimaryLight);

        isShowWeekLabel = DEFAULT_SHOW_WEEKLABEL;
        bgColorOfWeek = DEFAULT_BG_WEEKLABEL;
        invalidCellTextColor = DEFAULT_INVALID_CELL_TEXTCOLOR;
        validCellTextColor = DEFAULT_VALID_CELL_TEXTCOLOR;
        bgOfSelectedCell = DEFAULT_BG_COLOR_TODAY;
        bgColorOfToday = DEFAULT_BG_COLOR_TODAY;
        todayTextColor = DEFAULT_TODAY_TEXT_COLOR;
    }

    /**
     * init the weeklabel(Sun,Mon,Tue,Wed,Thu,Fri,Sat)
     */
    private void initWeekLable(Context context) {
        if (isShowWeekLabel) {
            mWeekLabel = new WeekLabel(context);
            mWeekLabel.setBackgroundColor(bgColorOfWeek);
            LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT);
            addView(mWeekLabel, params);
        }
    }

    private void initCellGridView(Context context) {
        cellsGridView = new GridView(context);
        cellsGridView.setNumColumns(CALENDAR_CELL_COLUMNS);
        setCalendarCellLines();
        cellsGridView.setOnItemClickListener(new OnCalendarCellClickListener());
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        addView(cellsGridView, params);
        updateCells();
    }

    private void setCalendarCellLines() {
        if (isShowLines) {
            cellsGridView.setVerticalSpacing(lineWidth);
            cellsGridView.setHorizontalSpacing(lineWidth);
            cellsGridView.setBackgroundColor(lineColor);
            cellsGridView
                    .setPadding(lineWidth, lineWidth, lineWidth, lineWidth);
        } else {
            cellsGridView.setVerticalSpacing(0);
            cellsGridView.setHorizontalSpacing(0);
            cellsGridView.setBackgroundDrawable(null);
            cellsGridView.setPadding(0, 0, 0, 0);
        }
    }

    private void updateCells() {
        calculateDays();
        cellsGridView.setAdapter(new CalendarCellAdapter(getContext()));
    }

    public void setMonthTermine(ArrayList<Termin> termine){
        this.termine = new ArrayList<>();
        this.termine = (ArrayList<Termin>) termine.clone();
    }

    /**
     * calculate the days of this month
     */
    private void calculateDays() {
        mDays = new String[CALENDAR_CELL_COLUMNS * CALENDAR_CELL_ROWS];
        int currentYear = currentCalendar.get(Calendar.YEAR);
        int currentMonth = currentCalendar.get(Calendar.MONTH);
        int firstDayOfMonth = currentCalendar
                .getActualMinimum(Calendar.DAY_OF_MONTH);
        int lastDayOfMonth = currentCalendar
                .getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar tempCalendar = Calendar.getInstance();

        tempCalendar.set(Calendar.DAY_OF_MONTH, firstDayOfMonth);
        tempCalendar.set(Calendar.MONTH, currentMonth);

		/* the day-of-week of current month */
        int firstDayWeekInMonth = tempCalendar.get(Calendar.DAY_OF_WEEK);

        firstDayPos = firstDayWeekInMonth - 1;
        todayPos = firstDayPos
                + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) - 1;
        lastDayPos = firstDayPos + lastDayOfMonth - 1;
		/* number of current month days */
        for (int i = firstDayOfMonth; i <= lastDayOfMonth; i++) {
            mDays[firstDayPos + i - 1] = i + "";
        }
        Calendar lastMonth = Calendar.getInstance();
        if (currentMonth == 0) {// January
            lastMonth.set(Calendar.YEAR, currentYear - 1);
            lastMonth.set(Calendar.MONTH, 11);// last month of last year
        } else {
            lastMonth.set(Calendar.YEAR, currentYear);
            lastMonth.set(Calendar.MONTH, currentMonth - 1);

        }

        int lastDayOfLastMonth = lastMonth
                .getActualMaximum(Calendar.DAY_OF_MONTH);
		/* last month days */
        for (int i = 0; i < firstDayPos; i++) {
            mDays[i] = (lastDayOfLastMonth - firstDayPos + i + 1) + "";
        }
		/* number of next month days */
        int daysCountOfNextMonth = CALENDAR_CELL_COLUMNS * CALENDAR_CELL_ROWS
                - lastDayPos;
        for (int i = 1; i < daysCountOfNextMonth; i++) {
            mDays[lastDayPos + i] = i + "";
        }
    }

    /**
     * change current Calendar
     *
     * @param calendar
     */
    public void setCalendar(Calendar calendar) {
        if (minCalendar == null || maxCalendar == null) {
            minCalendar = calendar;
            maxCalendar = calendar;
        }
        if (calendar.compareTo(minCalendar) < 0
                || calendar.compareTo(maxCalendar) > 0) {
            throw new IllegalArgumentException(
                    "Error:current calendar is out of range !");
        }
        this.currentCalendar = calendar;
        updateCells();
    }

    public Calendar getCalendar() {
        return currentCalendar;
    }

    public Calendar getMaxCalendar() {
        return maxCalendar;
    }

    public Calendar getMinCalendar() {
        return minCalendar;
    }

    public void setMinMaxCalendar(Calendar minCalendar, Calendar maxCalendar) {
        if (minCalendar.compareTo(maxCalendar) > 0) {
            throw new IllegalArgumentException(
                    " Error: the minimun calendar is larger than maximum calendar !");
        }
        this.minCalendar = minCalendar;
        this.maxCalendar = maxCalendar;
    }

    public void showNextMonth() {
        if (currentCalendar.compareTo(maxCalendar) > 0) {
            return;
        }
        int currentMonth = currentCalendar.get(Calendar.MONTH);
		/*
		 * if current month is December ,the set current month to January ,and
		 * current year plus 1
		 */
        if (currentMonth == 11) {// December
            this.currentCalendar.add(Calendar.YEAR, 1);// add year
            this.currentCalendar.set(Calendar.MONTH, 0);// set month to January
        } else {
            this.currentCalendar.add(Calendar.MONTH, 1);
        }
        updateCells();
    }

    public void showLastsMonth() {
        if (currentCalendar.compareTo(minCalendar) < 0) {
            return;
        }
        int currentMonth = currentCalendar.get(Calendar.MONTH);
		/*
		 * if current month is January ,then set current month to December,and
		 * currnet year minus 1
		 */
        if (currentMonth == 0) {
            this.currentCalendar.add(Calendar.YEAR, -1);
            this.currentCalendar.set(Calendar.MONTH, 11);
        } else {
            this.currentCalendar.add(Calendar.MONTH, -1);
        }
        updateCells();
    }

    public void hideWeekLabel() {
        this.isShowWeekLabel = false;
        this.removeView(mWeekLabel);
    }

    public void setInvalidCellTextColor(int invalidCellTextColor) {
        this.invalidCellTextColor = invalidCellTextColor;
    }

    public void setValidCellTextColor(int validCellTextColor) {
        this.validCellTextColor = validCellTextColor;
    }

    public void setShowLines(boolean isShowLines) {
        this.isShowLines = isShowLines;
        setCalendarCellLines();
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public void setbgColorOfWeek(int bgColorOfWeekLabel) {
        this.bgColorOfWeek = bgColorOfWeekLabel;
    }

    public void setBgOfSelectedCell(int bgColorOfSelectedCell) {
        this.bgOfSelectedCell = bgColorOfSelectedCell;
    }

    public void setbgColorOfToday(int bgColorOfToday) {
        this.bgColorOfToday = bgColorOfToday;
    }

    public void setParentFragment(Fragment02 mFragment02){
        this.mFragment02 = mFragment02;
    }



    private class CalendarCellAdapter extends BaseAdapter {
        private LayoutInflater inflater;

        public class CalendarCell {
            public TextView tv;

        }

        public CalendarCellAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mDays.length;
        }

        @Override
        public Object getItem(int position) {
            return mDays[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CalendarCell cell;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.customcalendar_celllabel, null);
                cell = new CalendarCell();
                cell.tv = (TextView) convertView
                        .findViewById(R.id.calendar_cell_tv);


            } else {
                cell = (CalendarCell) convertView.getTag();
            }
            if (position < firstDayPos || position > lastDayPos) {
                cell.tv.setTextColor(invalidCellTextColor);
            } else {
                cell.tv.setTextColor(validCellTextColor);
            }

            if ((todayPos == position)
                    && (currentCalendar.get(Calendar.MONTH) == Calendar
                    .getInstance().get(Calendar.MONTH))) {
                cell.tv.setTextColor(todayTextColor);
            }
            String day = mDays[position];

            if(position >= firstDayPos && position <= lastDayPos){
                final ArrayList<Termin> tagesTermine = new ArrayList<>();
                int dayOfMonth = Integer.parseInt(day);
                for(int i = 0; i < termine.size(); i++){
                    if(getCalendar().get(Calendar.YEAR) >= termine.get(i).getBeginn().get(Calendar.YEAR) &&
                            getCalendar().get(Calendar.YEAR) <= termine.get(i).getEnde().get(Calendar.YEAR) &&
                            getCalendar().get(Calendar.MONTH) >= termine.get(i).getBeginn().get(Calendar.MONTH) &&
                            getCalendar().get(Calendar.MONTH) <= termine.get(i).getEnde().get(Calendar.MONTH) &&
                            dayOfMonth >= termine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) &&
                            dayOfMonth <= termine.get(i).getEnde().get(Calendar.DAY_OF_MONTH)){

                        tagesTermine.add(termine.get(i));
                    }
                }
                if(tagesTermine.size() == 1){
                    //If just Termin, show the current Besetzung Icon
                    if(tagesTermine.get(0).getTeilnahme() == Terminteilnahme.NIEMAND){
                        Drawable highlight = getContext().getResources().getDrawable( R.drawable.calendar_ter_rot );
                        ((TextView) convertView).setCompoundDrawablesWithIntrinsicBounds(null, null, null, highlight);
                    }
                    else{
                        if(tagesTermine.get(0).getTeilnahme() == Terminteilnahme.WENIGE){
                            Drawable highlight = getContext().getResources().getDrawable( R.drawable.calendar_ter_gelb );
                            ((TextView) convertView).setCompoundDrawablesWithIntrinsicBounds(null, null, null, highlight);
                        }
                        else{
                            if(tagesTermine.get(0).getTeilnahme() == Terminteilnahme.VOLL){
                                Drawable highlight = getContext().getResources().getDrawable( R.drawable.calendar_ter_gruen );
                                ((TextView) convertView).setCompoundDrawablesWithIntrinsicBounds(null, null, null, highlight);
                            }
                        }
                    }
                }
                else{
                    //If there are more than one Termin per Day, generate Iconcode, depending
                    //on current Besetzungsstatus, sort by red->yellow->green
                    //But show max 3 Icons
                    String iconSelector = "";
                    if(tagesTermine.size() > 1){
                        for(int i = 0; i < tagesTermine.size() && i < 4; i++){
                            if(tagesTermine.get(i).getTeilnahme() == Terminteilnahme.NIEMAND){
                                iconSelector = "r" + iconSelector;
                            }
                            else{
                                if(tagesTermine.get(i).getTeilnahme() == Terminteilnahme.WENIGE){
                                    if(iconSelector.length() == 0){
                                        iconSelector = "y";
                                    }
                                    else{
                                        if(iconSelector.equals("r") || iconSelector.equals("rr")){
                                            iconSelector = iconSelector + "y";
                                        }
                                        else{
                                            if(iconSelector.equals("g") || iconSelector.equals("gg")){
                                                iconSelector = "y" + iconSelector;
                                            }
                                            else{
                                                iconSelector = "ryg";
                                            }
                                        }
                                    }
                                }
                                else{
                                    if(tagesTermine.get(i).getTeilnahme() == Terminteilnahme.VOLL){
                                        iconSelector = iconSelector + "g";
                                    }
                                }
                            }
                        }
                        //Select your Drawable Image, due to the corresponding Iconcode
                        Drawable highlight;
                        switch (iconSelector){
                            case "rr": highlight = getContext().getResources().getDrawable( R.drawable.ter_rr ); break;
                            case "ry": highlight = getContext().getResources().getDrawable( R.drawable.ter_ry ); break;
                            case "rg": highlight = getContext().getResources().getDrawable( R.drawable.ter_rg ); break;
                            case "yy": highlight = getContext().getResources().getDrawable( R.drawable.ter_yy ); break;
                            case "yg": highlight = getContext().getResources().getDrawable( R.drawable.ter_yg ); break;
                            case "gg": highlight = getContext().getResources().getDrawable( R.drawable.ter_gg ); break;
                            case "rrr": highlight = getContext().getResources().getDrawable( R.drawable.ter_rrr ); break;
                            case "rry": highlight = getContext().getResources().getDrawable( R.drawable.ter_rry ); break;
                            case "rrg": highlight = getContext().getResources().getDrawable( R.drawable.ter_rrg ); break;
                            case "ryy": highlight = getContext().getResources().getDrawable( R.drawable.ter_ryy ); break;
                            case "ryg": highlight = getContext().getResources().getDrawable( R.drawable.ter_ryg ); break;
                            case "rgg": highlight = getContext().getResources().getDrawable( R.drawable.ter_rgg ); break;
                            case "yyy": highlight = getContext().getResources().getDrawable( R.drawable.ter_yyy ); break;
                            case "yyg": highlight = getContext().getResources().getDrawable( R.drawable.ter_yyg ); break;
                            case "ygg": highlight = getContext().getResources().getDrawable( R.drawable.ter_ygg ); break;
                            default: highlight = getContext().getResources().getDrawable( R.drawable.ter_ggg ); break;
                        }
                        ((TextView) convertView).setCompoundDrawablesWithIntrinsicBounds(null, null, null, highlight);
                    }


                }
            }

            cell.tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.nfh_calendar));
            cell.tv.setText(day);
            convertView.setTag(cell);
            return convertView;
        }
    }

    private class OnCalendarCellClickListener implements OnItemClickListener {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            if ((position >= firstDayPos) && (position <= lastDayPos)) {
                if (parent.getTag() != null) {
                    ((View) parent.getTag()).setBackgroundColor(bgOfNormalCell);

                }
                parent.setTag(view);
                Drawable d = ContextCompat.getDrawable(getContext(), R.drawable.default_bg_selected_cell);
                view.setBackground(d);

                currentCalendar.set(Calendar.DAY_OF_MONTH, new Integer(
                        mDays[position]));
                mOnDateSelectedListener.onDateSelected(currentCalendar);

                final ArrayList<Termin> tagesTermine = new ArrayList<>();
                for(int i = 0; i < termine.size(); i++){
                    if(getCalendar().get(Calendar.YEAR) >= termine.get(i).getBeginn().get(Calendar.YEAR) &&
                            getCalendar().get(Calendar.YEAR) <= termine.get(i).getEnde().get(Calendar.YEAR) &&
                            getCalendar().get(Calendar.MONTH) >= termine.get(i).getBeginn().get(Calendar.MONTH) &&
                            getCalendar().get(Calendar.MONTH) <= termine.get(i).getEnde().get(Calendar.MONTH) &&
                            getCalendar().get(Calendar.DAY_OF_MONTH) >= termine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) &&
                            getCalendar().get(Calendar.DAY_OF_MONTH) <= termine.get(i).getEnde().get(Calendar.DAY_OF_MONTH)){

                        tagesTermine.add(termine.get(i));
                    }
                }
                if(tagesTermine.size() == 0){
                    Toast tNoConnection = Toast.makeText(mFragment02.getContext(), "Kein Termin an diesem Tag", Toast.LENGTH_LONG);
                    tNoConnection.show();
                }
                else{
                    if(tagesTermine.size() == 1){
                        sendRequest(tagesTermine.get(0).getId());
                    }
                    else{
                        final Dialog dialog = new Dialog(mFragment02.getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.dialog_show_calendar_day);


                        TextView tvTitle = (TextView) dialog.findViewById(R.id.TextView01);
                        tvTitle.setText("Termine am " + currentCalendar.get(Calendar.DAY_OF_MONTH) + "." +
                                currentCalendar.get(Calendar.MONTH) + "." +
                                currentCalendar.get(Calendar.YEAR));

                        ListView lv1 = (ListView) dialog.findViewById(R.id.listView1);

                        String lv1_values1[] = new String[tagesTermine.size()];
                        String lv1_values3[] = new String[tagesTermine.size()];
                        for(int i = 0; i < lv1_values1.length; i++){
                            lv1_values1[i] = tagesTermine.get(i).getBezeichnung();
                            lv1_values3[i] = tagesTermine.get(i).getBeginn().get(Calendar.DAY_OF_MONTH) + "." +
                                    (tagesTermine.get(i).getBeginn().get(Calendar.MONTH)+1) + "." +
                                    tagesTermine.get(i).getBeginn().get(Calendar.YEAR);
                        }
                        int lv1_values2[] = new int[tagesTermine.size()];
                        for(int i = 0; i < lv1_values2.length; i++){
                            if(tagesTermine.get(i).getTeilnahme() == Terminteilnahme.NIEMAND){
                                lv1_values2[i] = R.drawable.ter_rot;
                            }
                            else{
                                if(tagesTermine.get(i).getTeilnahme() == Terminteilnahme.WENIGE){
                                    lv1_values2[i] = R.drawable.ter_gelb;
                                }
                                else{
                                    lv1_values2[i] = R.drawable.ter_gruen;
                                }
                            }
                        }
                        ListViewImageAdapter adapter = new ListViewImageAdapter(mFragment02.getActivity(), lv1_values1, lv1_values2, lv1_values3);
                        lv1.setAdapter(adapter);
                        lv1.setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                dialog.cancel();
                                sendRequest(tagesTermine.get(position).getId());

                            }
                        });
                        dialog.show();
                    }
                }
            }
        }

        private void sendRequest(String terId){
            //Set Request Data for Backend Connection
            String[][] requestData = new String[1][2];
            requestData[0][0] = "terminId";
            requestData[0][1] = terId;

            //Get Data from Backend
            HTTPRequester myRequester = new HTTPRequester(requestData, "http://mf-multimedia.de/kunden/DRK/DRK_ENTWICKLUNG/mobile_termin.php");
            String response = "";
            try{
                response = myRequester.execute().get();
            }catch (Exception e){
                e.printStackTrace();
            }

            mFragment023.setData(StringOperator.extractBetweenSpaces(response));
            mFragment023.buildDataModel();
            FragmentManager fragmentManager = mFragment02.getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, mFragment023).addToBackStack("appointments").commit();
        }
    }

    private class WeekLabel extends GridView {
        public WeekLabel(Context context) {
            super(context, null);
            String[] tempWeekdays = new DateFormatSymbols().getShortWeekdays();
            int n = tempWeekdays.length - 1;
            String weekdays[] = new String[n];
            for (int i = 0; i < n; i++) {
                weekdays[i] = tempWeekdays[i + 1];
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                    R.layout.customcalendar_weeklabel, weekdays);
            LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT);
            this.setLayoutParams(params);
            this.setGravity(Gravity.CENTER);
            this.setNumColumns(CALENDAR_CELL_COLUMNS);
            this.setAdapter(adapter);
        }
    }



}
