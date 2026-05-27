package com.example.lab5;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int numberOfElements = 100;
    public static volatile int sortDelay = 50;

    SortView sortView;
    Spinner algorithmSpinner;
    int[] array;

    boolean isSorting = false;
    volatile boolean cancelSorting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sortView = findViewById(R.id.sortView);
        algorithmSpinner = findViewById(R.id.algorithmSpinner);

        String[] algorithms = {"Quick Sort", "Merge Sort"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, algorithms);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        algorithmSpinner.setAdapter(adapter);

        Button generateButton = findViewById(R.id.generateButton);
        generateButton.setOnClickListener(v -> {
            if (isSorting) {
                Toast.makeText(this, R.string.sorting_running_error_message, Toast.LENGTH_SHORT).show();
                return;
            }

            array = ArrayGenerator.generateArray(numberOfElements);
            sortView.setArray(array);
        });

        Button sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(v -> {

            if (isSorting) {
                Toast.makeText(this, R.string.sorting_running_error_message, Toast.LENGTH_SHORT).show();
                return;
            }

            if (array == null || array.length == 0) {
                Toast.makeText(this, R.string.no_array_error_text, Toast.LENGTH_SHORT).show();
                return;
            }

            sortView.setArray(array);

            cancelSorting = false;
            isSorting = true;

            String selected = algorithmSpinner.getSelectedItem().toString();

            new Thread(() -> {

                if (selected.equals("Quick Sort")) {
                    Sorts.quickSort(array, 0, array.length - 1, () -> {

                        if (cancelSorting) return;
                        runOnUiThread(() -> {
                            sortView.invalidate();
                        });

                    }, () -> cancelSorting);
                }

                if (selected.equals("Merge Sort")) {
                    Sorts.mergeSort(array, 0, array.length - 1, () -> {

                        if (cancelSorting) return;
                        runOnUiThread(() -> {
                            sortView.invalidate();
                        });
                    }, () -> cancelSorting);
                }

                runOnUiThread(() -> {
                    isSorting = false;
                });

            }).start();
        });


        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(v -> {

            if (!isSorting) {
                Toast.makeText(this, R.string.sort_not_started_error_message, Toast.LENGTH_SHORT).show();
                return;
            }

            cancelSorting = true;
        });

        SeekBar speedSeekBar = findViewById(R.id.speedSeekBar);
        speedSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sortDelay = Math.max(1, 200 - progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }
}