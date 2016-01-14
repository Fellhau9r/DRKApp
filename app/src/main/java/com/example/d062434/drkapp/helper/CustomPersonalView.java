package com.example.d062434.drkapp.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;

import com.example.d062434.drkapp.R;

/**
 * Created by D062434 on 26.10.2015.
 */
public class CustomPersonalView extends View{
    private ShapeDrawable mShapePersonal;
    private ShapeDrawable mShapeChartBorder;

    private int minPersonal;
    private int maxPersonal;
    private int currentPersonal;
    private int screenSize;


    public CustomPersonalView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    protected void onDraw(Canvas canvas){
        drawChartLines(canvas);
        drawCurrentPers(canvas);

    }

    private void drawCurrentPers(Canvas canvas){
        int x = 10;
        int y = 20;
        int height = 20;

        int width = screenSize / maxPersonal;
        width = width * currentPersonal;
        mShapePersonal = new ShapeDrawable(new RectShape());
        mShapePersonal.getPaint().setColor(getResources().getColor(R.color.colorAccent));
        mShapePersonal.setBounds(x, y, x + width, y + height);

        mShapePersonal.draw(canvas);
    }

    private void drawChartLines(Canvas canvas){
        int width = screenSize / maxPersonal;
        int x = 10;
        int y = 10;
        int height = 40;

        int MY_DIP_VALUE = 12; //5dp
        int pixel= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                MY_DIP_VALUE, getResources().getDisplayMetrics());


        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setTextSize(pixel);

        for(int i = 0; i < maxPersonal; i++){
            mShapeChartBorder = new ShapeDrawable(new RectShape());
            mShapeChartBorder.getPaint().setColor(Color.LTGRAY);
            mShapeChartBorder.setBounds(x, y, x + width, y + height);
            mShapeChartBorder.draw(canvas);
            if(maxPersonal > 10){
                if((i % 5) == 0){
                    canvas.drawText(i + "", x, y + height + pixel + 10, paint);
                }
            }
            else{
                canvas.drawText(i + "", x, y + height + pixel + 10, paint);
            }

            mShapeChartBorder = new ShapeDrawable(new RectShape());
            mShapeChartBorder.getPaint().setColor(Color.WHITE);
            mShapeChartBorder.setBounds(x + 3, y + 3, x + width - 3, y + height - 3);
            mShapeChartBorder.draw(canvas);

            x = x + width;
        }
        int pixelMargin;
        if(maxPersonal > 9){
            if(maxPersonal % 5 == 0){
                pixelMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        10, getResources().getDisplayMetrics());
                canvas.drawText(maxPersonal + "", x-pixelMargin, y + height + pixel + 10, paint);
            }
        }
        else{
            pixelMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    7, getResources().getDisplayMetrics());
            canvas.drawText(maxPersonal + "", x-pixelMargin, y + height + pixel + 10, paint);
        }

    }

    public void setPersonal(int min, int max, int current){
        minPersonal = min;
        //Wenn max unbegrenzt oder sehr groß, ist die Mindestanzahl die Maßgebende Einheit
        if(max > 99 || current > max){
            //Wenn mehr Personen eingetragen sind als nötig, richtet sich die Skala nach den
            //bisher eingetragenen Personen
            if(current > min){
                maxPersonal = current;
            }
            else{
                maxPersonal = min;
            }
        }
        else{
            maxPersonal = max;
        }
        currentPersonal = current;
    }

    public void setScreenSize(int screenSize){
        int MY_DIP_VALUE = 30;
        int pixel= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                MY_DIP_VALUE, getResources().getDisplayMetrics());
        this.screenSize = screenSize - pixel;
    }
}
