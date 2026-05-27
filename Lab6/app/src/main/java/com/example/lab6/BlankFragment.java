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

public class BlankFragment extends Fragment {

    private SharedViewModel sharedViewModel;
    
    public BlankFragment() {
        // Required empty public constructor
    }

    public static BlankFragment newInstance() {
        return new BlankFragment();
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
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btn1 = view.findViewById(R.id.button1to2);
        Button btn2 = view.findViewById(R.id.button1toActivity);
        TextView textView = view.findViewById(R.id.text1);

        sharedViewModel.getMessageToFragment1().observe(getViewLifecycleOwner(), textView::setText);
        
        btn1.setOnClickListener(v ->
                sharedViewModel.setMessageToFragment2("Hello from Fragment 1!")
        );
        btn2.setOnClickListener(v ->
                sharedViewModel.setMessageToActivity("Hello to Activity from Fragment 1!")
        );
    }
}