package com.example.lab2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        TextView textView = findViewById(R.id.textView);
        String message = getIntent().getStringExtra("message");
        textView.setText(message);

        Button btnBack = findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(View -> finish());
    }
}