package com.demo.sp19.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.demo.architect.data.model.offline.BackgroundDialModel;
import com.demo.architect.data.model.offline.GiftModel;
import com.demo.architect.utils.view.FileUtils;
import com.demo.sp19.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

public class RotationView extends View {

    private int height, width = 0;
    private int padding = 0;
    private int fontSize = 0;
    private int numeralSpacing = 0;
    private int handTruncation, hourHandTruncation = 0;
    private float radius = 0;
    private Paint paint;
    private boolean isInit;
    private int number = 8;
    private BackgroundDialModel backgroundDialModel;
    private List<GiftModel> giftModels = new ArrayList<>();
    private Rect rect = new Rect();

    public RotationView(Context context) {
        super(context);
    }

    public RotationView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RotationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBackgroundDialModel(BackgroundDialModel backgroundDialModel) {
        this.backgroundDialModel = backgroundDialModel;
        requestLayout();
    }

    public void setGiftModels(List<GiftModel> list) {
        giftModels.clear();
        giftModels.addAll(list);
        giftModels.addAll(list);
        if (list.size() <3){
            giftModels.addAll(list);
        }
        Collections.reverse(giftModels);
        number = giftModels.size();
        requestLayout();
    }

    private void initClock() {
        height = getHeight();
        width = getWidth();
        // padding = numeralSpacing + 50;
        fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13,
                getResources().getDisplayMetrics());
        int min = Math.min(height, width);
        radius = min / 2.5f;
        handTruncation = min / 20;
        hourHandTruncation = min / 7;
        paint = new Paint();
        isInit = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!isInit) {
            initClock();
        }
        if (backgroundDialModel != null) {
            drawBitmap(canvas);
            drawCircleBig(canvas);
        }
        drawCircle(canvas);

        if (giftModels.size() > 0) {
            drawNumeral(canvas);
            drawHands(canvas);
        }

    }

    private void drawHand(Canvas canvas, int loc) {
        paint.setColor(getResources().getColor(android.R.color.white));
        double angle = Math.PI / (number / 2) * (loc);
        float handRadius = radius - 13;
        //x = R*cos(angle)
        canvas.drawLine(width / 2, height / 2,
                (float) (width / 2 + Math.cos(angle) * handRadius),
                (float) (height / 2 + Math.sin(angle) * handRadius),
                paint);
    }

    private void drawImage(Canvas canvas, String imagePath, int loc) {

        paint.setColor(getResources().getColor(android.R.color.white));
        double angle = Math.PI / (number / 2) * (loc) - Math.PI / (number);
        float handRadius = (radius - ((radius) / 3));
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        bitmap = FileUtils.resizeImage(bitmap, (int) (handRadius / 1.5), (int) (handRadius / 1.5));
        Matrix matrix = new Matrix();
        matrix.setRotate((360 / number) * (loc - 0.5f) + 90);
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap1.setHasAlpha(true);
        float x = (float) (width / 2 + Math.cos(angle) * handRadius);
        float y = (float) (height / 2 + Math.sin(angle) * handRadius);

        canvas.drawBitmap(bitmap1, x - bitmap1.getWidth() / 2, y - bitmap1.getHeight() / 2, paint);
    }


    private void drawHands(Canvas canvas) {
        for (int i = 1; i <= number; i++) {
            drawHand(canvas, i);
            drawImage(canvas, giftModels.get(i - 1).getFilePath(), i);
        }
    }


    private void drawNumeral(Canvas canvas) {
        //  paint.setTextSize(fontSize);
        for (int i = 1; i <= number; i++) {
            paint.setColor(Color.parseColor(backgroundDialModel.getColorBorder()));
            paint.setStrokeWidth(5);
            paint.setStyle(Paint.Style.STROKE);
            paint.setAntiAlias(true);
            //    paint.getTextBounds(tmp, 0, tmp.length(), rect);
            double angle = Math.PI / (number / 2) * (i);
            int x = (int) (width / 2 + Math.cos(angle) * radius - rect.width() / 2);
            int y = (int) (height / 2 + Math.sin(angle) * radius + rect.height() / 2);

            canvas.drawCircle(x, y, 5f, paint);
        }
    }

    private void drawCenter(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(width / 2, height / 2, radius / 3, paint);
    }

    private void drawCircle(Canvas canvas) {
        paint.reset();
        paint.setColor(getResources().getColor(android.R.color.white));
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()));
        paint.setStrokeWidth(px);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, radius + 5, paint);
    }

    private void drawCircleBig(Canvas canvas) {
        paint.reset();
        paint.setColor(Color.parseColor(backgroundDialModel.getColorBorder()));
        int px = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
        paint.setStrokeWidth(px);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        canvas.drawCircle(width / 2, height / 2, radius, paint);
    }

    private void drawBitmap(Canvas canvas) {
        paint.reset();
        Bitmap bitmap = BitmapFactory.decodeFile(backgroundDialModel.getBGRing());
        bitmap = FileUtils.resizeImage(bitmap, (int) (radius * 2), (int) (radius * 2));
        canvas.drawBitmap(bitmap, width / 2 - bitmap.getWidth() / 2, height / 2 - bitmap.getHeight() / 2, paint);
    }

}
