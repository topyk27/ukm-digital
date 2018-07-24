package com.example.topyk.ukmdigital.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;

import static android.R.attr.height;
import static android.R.attr.width;

/**
 * Created by topyk on 9/9/2017.
 */

public class BMP {

    public BMP(){

    }
    public Bitmap bmp (Bitmap bitmap){
        Bitmap background = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
        float oriWidth = bitmap.getWidth();
        float oriHeight = bitmap.getHeight();

        Canvas canvas = new Canvas(background);
        float scale = width / oriWidth;

        float xTrans = 0.0f;
        float yTrans = (height - oriHeight * scale) / 2.0f;

        Matrix transformation = new Matrix();
        transformation.postTranslate(xTrans,yTrans);
        transformation.preScale(scale,scale);

        Paint paint = new Paint();
        paint.setFilterBitmap(true);

        canvas.drawBitmap(bitmap,transformation,paint);
        return background;

    }
}
