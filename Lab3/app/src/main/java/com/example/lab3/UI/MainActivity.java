package com.example.lab3.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab3.R;
import com.example.lab3.UI.Fragments.MovieListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, MovieListFragment.newInstance())
                    .commit();
        }
    }
}