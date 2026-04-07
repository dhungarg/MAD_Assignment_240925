package com.example.q1;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.materialswitch.MaterialSwitch;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        MaterialSwitch switchDark = findViewById(R.id.switchDark);

        SharedPreferences sp = getSharedPreferences(Q1Application.PREFS, MODE_PRIVATE);
        switchDark.setChecked(sp.getBoolean(Q1Application.KEY_DARK, false));

        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sp.edit().putBoolean(Q1Application.KEY_DARK, isChecked).apply();
            AppCompatDelegate.setDefaultNightMode(
                    isChecked ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
            );
        });
    }
}