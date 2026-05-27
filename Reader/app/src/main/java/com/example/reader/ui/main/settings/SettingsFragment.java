package com.example.reader.ui.main.settings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.example.reader.R;
import com.example.reader.infrastructure.managers.SettingsManager;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Objects;

public class SettingsFragment extends Fragment {
    private SettingsManager settingsManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsManager = new SettingsManager(requireContext());

        SwitchMaterial swOverride = view.findViewById(R.id.swOverrideGlobal);
        SeekBar sbTextSize = view.findViewById(R.id.sbTextSize);
        RadioGroup rgFonts = view.findViewById(R.id.rgFonts);
        RadioGroup rgThemes = view.findViewById(R.id.rgThemes);

        // Инициализация текущих значений
        swOverride.setChecked(settingsManager.isGlobalOverride());
        sbTextSize.setProgress(settingsManager.getGlobalTextSize());
        
        if (Objects.equals(settingsManager.getGlobalFont(), "serif")) rgFonts.check(R.id.rbSerif);
        else rgFonts.check(R.id.rbSans);
        
        switch (settingsManager.getGlobalTheme()) {
            case "white":
                rgThemes.check(R.id.rbWhite);
                break;
            case "sepia":
                rgThemes.check(R.id.rbSepia);
                break;
            case "night":
                rgThemes.check(R.id.rbNight);
                break;
        }

        
        swOverride.setOnCheckedChangeListener((btn, isChecked) -> {
            settingsManager.setGlobalOverride(isChecked);
        });

        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    settingsManager.setGlobalTextSize(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        rgFonts.setOnCheckedChangeListener((group, checkedId) -> {
            String FontFamily = (checkedId == R.id.rbSerif) ? "serif" : "sans-serif";
            settingsManager.setGlobalFont(FontFamily);
        });
        
        rgThemes.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbWhite) {
                settingsManager.setGlobalTheme("white");
            } else if (checkedId == R.id.rbSepia) {
                settingsManager.setGlobalTheme("sepia");
            } else if (checkedId == R.id.rbNight) {
                settingsManager.setGlobalTheme("night");
            }
        });

        return view;
    }
}