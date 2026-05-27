package com.example.lab6;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class BlankFragment2 extends Fragment {

    private SharedViewModel sharedViewModel;

    public BlankFragment2() {
        // Required empty public constructor
    }

    public static BlankFragment2 newInstance() {
        return new BlankFragment2();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn1 = view.findViewById(R.id.button2to1);
        Button btn2 = view.findViewById(R.id.button2toActivity);
        TextView textView = view.findViewById(R.id.text2);

        sharedViewModel.getMessageToFragment2().observe(getViewLifecycleOwner(), textView::setText);

        btn1.setOnClickListener(v ->
                sharedViewModel.setMessageToFragment1("Hello from Fragment 2!")
        );
        btn2.setOnClickListener(v ->
                sharedViewModel.setMessageToActivity("Hello to Activity from Fragment 2!")
        );
    }
}