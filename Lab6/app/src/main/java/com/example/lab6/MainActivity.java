package com.example.lab6;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity {

    private SharedViewModel sharedViewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        GetFragment(R.id.frag1, BlankFragment.newInstance());
        GetFragment(R.id.frag2, BlankFragment2.newInstance());
        TextView textView = findViewById(R.id.text);

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        sharedViewModel.getMessageToActivity().observe(this, textView::setText);

    }
    
    private void GetFragment(int holder, Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(holder, fragment)
                .commit();
    }
}