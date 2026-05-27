package com.example.ex4;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = findViewById(R.id.drawingView);

        findViewById(R.id.btnUndo).setOnClickListener(v -> drawingView.undo());
        findViewById(R.id.btnRedo).setOnClickListener(v -> drawingView.redo());
        findViewById(R.id.btnDraw).setOnClickListener(v -> drawingView.setEraser(false));
        findViewById(R.id.btnErase).setOnClickListener(v -> drawingView.setEraser(true));
    }
}