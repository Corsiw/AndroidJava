package com.example.ex2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;
    private ImageView imageView;
    private Button btnReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView2);
        btnReturn = findViewById(R.id.btnReturn);

        Intent intent = getIntent();
        
        String text = intent.getStringExtra("text");
        textView.setText(text);
        
        String uriString = intent.getStringExtra("imageUri");

        if (uriString != null) {
            Uri uri = Uri.parse(uriString);
            imageView.setImageURI(uri);
        }

        btnReturn.setOnClickListener(v -> returnResult());
    }

    private void returnResult() {

        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", "Изображение получено!");

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}