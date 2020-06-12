package com.mcc.screenoverlayexample;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.andremion.counterfab.CounterFab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Created by anupamchugh on 01/08/17.
 */

public class FloatingWidgetService extends Service {


    private WindowManager mWindowManager;
    private View mOverlayView;
    CounterFab counterFab;

    ImageView plusImage;

    public static final String TAG = FloatingWidgetService.class.getSimpleName();


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        setTheme(R.style.AppTheme);

        mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null);


        final WindowManager.LayoutParams params;

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                 WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |   WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;


        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mOverlayView, params);



        plusImage = (ImageView) mOverlayView.findViewById(R.id.plus_image);

        plusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG , "click");
            }
        });

        plusImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Log.d(TAG , "touch listener");
                return false;
            }
        });

        counterFab = (CounterFab) mOverlayView.findViewById(R.id.fabHead);
        counterFab.setCount(1);

        counterFab.setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:


                        Log.d(TAG , "down");

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();


                        return true;
                    case MotionEvent.ACTION_UP:

                        //Add code for launching application and positioning the widget to nearest edge.


                        Log.d(TAG , "up");

                        return true;
                    case MotionEvent.ACTION_MOVE:


                        Log.d(TAG , "move");

                        float Xdiff = Math.round(event.getRawX() - initialTouchX);
                        float Ydiff = Math.round(event.getRawY() - initialTouchY);


                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) Xdiff;
                        params.y = initialY + (int) Ydiff;

                        //Update the layout with new X & Y coordinates
                        mWindowManager.updateViewLayout(mOverlayView, params);


                        return true;
                }
                return false;
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mOverlayView != null)
            mWindowManager.removeView(mOverlayView);
    }

}
