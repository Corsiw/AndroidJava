package com.example.lab8.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab8.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private EditText emailEdit;
    private EditText passwordEdit;

    private Button register;

    FirebaseAuth auth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth = FirebaseAuth.getInstance();

        emailEdit = findViewById(R.id.emailEdit);
        passwordEdit = findViewById(R.id.passwordEdit);
        register = findViewById(R.id.registerButton);

        register.setOnClickListener(v -> {

            String email = emailEdit.getText().toString();
            String password = passwordEdit.getText().toString();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            handleRegisterError(task.getException());
                        }
                    });
        });
    }

    private void handleRegisterError(Exception exception) {
        if (exception instanceof FirebaseAuthWeakPasswordException) {
            FirebaseAuthWeakPasswordException e = (FirebaseAuthWeakPasswordException) exception;
            Toast.makeText(this, "Слишком слабый пароль: " + e.getReason(), Toast.LENGTH_LONG).show();
        } else if (exception instanceof FirebaseAuthInvalidCredentialsException) {
            Toast.makeText(this, "Неправильный формат email", Toast.LENGTH_LONG).show();
        } else if (exception instanceof FirebaseAuthUserCollisionException) {
            Toast.makeText(this, "Пользователь с таким email уже существует", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(this, "Ошибка регистрации: " + exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}