package com.thilo20.machikoropad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.Locale;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // set back button on action bar
        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // read current System locale
        String langSystem = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Resources res = getResources();
            Configuration conf = res.getConfiguration();
            langSystem = conf.getLocales().get(0).getISO3Language();
        }
        // try to restore language from saved settings
        SharedPreferences settings = getSharedPreferences("com.thilo20.machikoropad", MODE_PRIVATE);
        String langSaved = settings.getString("locale", langSystem);

        // wire UI buttons
        RadioButton rb = (RadioButton) findViewById(R.id.rbLanguageEn);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("en");
            }
        });
        if ("en".equals(langSaved)) {
            rb.setChecked(true);
        }

        rb = (RadioButton) findViewById(R.id.rbLanguageDe);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLocale("de");
            }
        });
        if ("de".equals(langSaved)) {
            rb.setChecked(true);
        }

    }

    public void setLocale(String lang) {
        // store language to saved settings
        SharedPreferences settings = getSharedPreferences("com.thilo20.machikoropad", MODE_PRIVATE);
        settings.edit().putString("locale", lang).apply();

        // apply selected language
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, SettingsActivity.class);
        finish();
        startActivity(refresh);
    }

    @Override
    public void onBackPressed() {
        // redirect to main menu
        onNavigateUp();
    }
}
