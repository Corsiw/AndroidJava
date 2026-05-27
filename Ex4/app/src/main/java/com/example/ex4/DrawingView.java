package com.example.ex4;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayDeque;
import java.util.Deque;

public class DrawingView extends View {
    private Paint paint;
    private Path currentPath;

    // Очереди для хранения истории (Механизм Undo/Redo)
    private final Deque<DrawAction> undoStack = new ArrayDeque<>();
    private final Deque<DrawAction> redoStack = new ArrayDeque<>();

    private static final int MAX_HISTORY_SIZE = 20; // Ограничение памяти
    private int currentColor = Color.BLACK;
    private float strokeWidth = 10f;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(currentColor);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        java.util.Iterator<DrawAction> it = undoStack.descendingIterator();
        while (it.hasNext()) {
            DrawAction action = it.next();
            paint.setColor(action.color);
            paint.setStrokeWidth(action.width);
            canvas.drawPath(action.path, paint);
        }
        if (currentPath != null) {
            paint.setColor(currentColor);
            paint.setStrokeWidth(strokeWidth);
            canvas.drawPath(currentPath, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                currentPath = new Path();
                currentPath.moveTo(x, y);
                redoStack.clear(); // Новое действие очищает Redo
                break;
            case MotionEvent.ACTION_MOVE:
                currentPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                currentPath.lineTo(x, y);
                undoStack.push(new DrawAction(currentPath, currentColor, strokeWidth));
                currentPath = null;

                // Механизм очистки памяти - удаляем самый старый путь
                if (undoStack.size() > MAX_HISTORY_SIZE) {
                    undoStack.removeLast();
                }
                break;
        }
        invalidate();
        return true;
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            redoStack.push(undoStack.pop());
            invalidate();
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            undoStack.push(redoStack.pop());
            invalidate();
        }
    }

    public void setEraser(boolean isEraser) {
        currentColor = isEraser ? Color.WHITE : Color.BLACK;
        strokeWidth = isEraser ? 50f : 10f;
    }
    
    private static class DrawAction {
        Path path;
        int color;
        float width;

        DrawAction(Path path, int color, float width) {
            this.path = path;
            this.color = color;
            this.width = width;
        }
    }
}