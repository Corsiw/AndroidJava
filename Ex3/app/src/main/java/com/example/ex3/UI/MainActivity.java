package com.example.ex3.UI;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ex3.R;
import com.example.ex3.UI.Fragments.TaskListFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, TaskListFragment.newInstance())
                    .commit();
        }
    }
}