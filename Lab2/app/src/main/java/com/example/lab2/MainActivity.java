package com.example.lab2;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editText = findViewById(R.id.editTextText);

        Button btn1 = findViewById(R.id.button1);
        Button btn2 = findViewById(R.id.button2);
        Button btn3 = findViewById(R.id.button3);

        btn1.setOnClickListener(view -> {
            // explicit intent
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);

            intent.putExtra("message", editText.getText().toString());
            startActivity(intent);
        });

        btn2.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString());
            startActivity(Intent.createChooser(intent, getString(R.string.chooser_title)));
        });

        btn3.setOnClickListener(View -> {

            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, editText.getText().toString());
            startActivity(intent);
        });

    }
}