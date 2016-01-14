package com.example.d062434.drkapp.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by D062434 on 29.10.2015.
 */
public class CustomNFHTextView extends TextView{
    private int posMitglied;
    private int posDay;

    public CustomNFHTextView(Context context) {
        super(context);
    }

    public CustomNFHTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomNFHTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomNFHTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public int getPosMitglied() {
        return posMitglied;
    }

    public void setPosMitglied(int posMitglied) {
        this.posMitglied = posMitglied;
    }

    public int getPosDay() {
        return posDay;
    }

    public void setPosDay(int posDay) {
        this.posDay = posDay;
    }
}
