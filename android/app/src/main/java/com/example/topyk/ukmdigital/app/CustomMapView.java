package com.example.topyk.ukmdigital.app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.google.android.gms.maps.MapView;

/**
 * Created by topyk on 10/21/2017.
 */

public class CustomMapView extends MapView {
    public CustomMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        int action = ev.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                // Disallow ScrollView to intercept touch events.
//                this.getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//
//            case MotionEvent.ACTION_UP:
//                // Allow ScrollView to intercept touch events.
//                this.getParent().requestDisallowInterceptTouchEvent(false);
//                break;
//        }
//
//        // Handle MapView's touch events.
//        super.dispatchTouchEvent(ev);
//        return true;
//    }
@Override
public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
        case MotionEvent.ACTION_UP:
            System.out.println("unlocked");
            this.getParent().requestDisallowInterceptTouchEvent(false);
            break;
        case MotionEvent.ACTION_DOWN:
            System.out.println("locked");
            this.getParent().requestDisallowInterceptTouchEvent(true);
            break;
    }
    return super.dispatchTouchEvent(ev);
}
}
