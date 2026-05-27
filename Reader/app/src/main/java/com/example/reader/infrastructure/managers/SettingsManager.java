package com.example.reader.infrastructure.managers;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String PREFS_NAME = "reader_settings";
    private final SharedPreferences prefs;

    public SettingsManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setGlobalOverride(boolean override) {
        prefs.edit().putBoolean("override_all", override).apply();
    }

    public boolean isGlobalOverride() {
        return prefs.getBoolean("override_all", false);
    }

    public void saveGlobalSettings(int textSize, String font, String theme) {
        prefs.edit()
                .putInt("global_text_size", textSize)
                .putString("global_font", font)
                .putString("global_theme", theme)
                .apply();
    }
    
    public int getGlobalTextSize() {
        return prefs.getInt("global_text_size", 18);
    }
    
    public void setGlobalTextSize(int textSize) {
        prefs.edit().putInt("global_text_size", textSize).apply();
    }
    
    public String getGlobalFont() {
        return prefs.getString("global_font", "serif");
    }

    public void setGlobalFont(String font) {
        prefs.edit().putString("global_font", font).apply();
    }
    
    public String getGlobalTheme() {
        return prefs.getString("global_theme", "light");
    }
    
    public void setGlobalTheme(String theme) {
        prefs.edit().putString("global_theme", theme).apply();
    }

}