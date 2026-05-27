package com.example.lab7;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {
    private CounterViewModel viewModel;
    private TextView counterText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        viewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        counterText = findViewById(R.id.text);
        button = findViewById(R.id.button);
        
        viewModel.getCounter().observe(this, value ->
                counterText.setText(String.valueOf(value)));

        viewModel.getIsRunning().observe(this, isRunning -> {
            if (isRunning) {
                button.setText(R.string.button_stop_text);
                button.setBackgroundColor(getColor(android.R.color.holo_red_dark));
            } else {
                button.setText(R.string.button_start_text);
                button.setBackgroundColor(getColor(android.R.color.holo_green_dark));
            }
        });

        button.setOnClickListener(v -> viewModel.toggleCounter());
    }
}