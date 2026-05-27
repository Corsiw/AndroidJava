package com.example.lab5;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;

public class SortView extends View {

    private int[] array;
    private final Paint paint;

    public SortView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
    }

    public void setArray(int[] arr) {
        this.array = arr;
        invalidate();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        if (array == null) return;
        
        paint.setColor(Color.BLUE);

        float width = getWidth() / (float) array.length;

        for (int i = 0; i < array.length; i++) {
            float height = array[i] * 5;
            canvas.drawRect(
                    i * width,
                    getHeight() - height,
                    (i + 1) * width,
                    getHeight(),
                    paint
            );
        }
    }
}