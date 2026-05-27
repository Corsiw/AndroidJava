package com.example.ex2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private ImageView imageView;
    private Button btnExplicit, btnImplicit, btnPickImage, btnImplicitExternal;

    private Uri imageUri;
    
    ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            imageUri = uri;
                            imageView.setImageURI(uri);
                        }
                    });
    
    ActivityResultLauncher<Intent> resultLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            String text = result.getData().getStringExtra("result");
                            editText.setText(text);
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        imageView = findViewById(R.id.imageView);
        
        btnPickImage = findViewById(R.id.btnPickImage);
        
        btnExplicit = findViewById(R.id.btnExplicit);
        btnImplicit = findViewById(R.id.btnImplicit);
        btnImplicitExternal = findViewById(R.id.btnImplicitExternal);
        

        btnPickImage.setOnClickListener(v -> pickImage());

        btnExplicit.setOnClickListener(v -> sendExplicit());
        btnImplicit.setOnClickListener(v -> sendImplicit());
        btnImplicitExternal.setOnClickListener(v -> sendImplicitExternal());
    }

    private void pickImage() {
        pickImageLauncher.launch("image/*");
    }

    private void sendExplicit() {

        Intent intent = new Intent(this, SecondActivity.class);

        intent.putExtra("text", editText.getText().toString());
        intent.putExtra("imageUri", imageUri.toString());

        resultLauncher.launch(intent);
    }

    private void sendImplicit() {

        Intent intent = new Intent("com.example.ex2.SEND_DATA");

        intent.putExtra("text", editText.getText().toString());

        if (imageUri != null)
            intent.putExtra("imageUri", imageUri.toString());

        resultLauncher.launch(intent);
    }

    private void sendImplicitExternal() {
        if (imageUri == null) return;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        
        intent.putExtra(Intent.EXTRA_TEXT, editText.getText().toString());
        
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        
        startActivity(Intent.createChooser(intent, "Отправить данные через:"));
    }
}