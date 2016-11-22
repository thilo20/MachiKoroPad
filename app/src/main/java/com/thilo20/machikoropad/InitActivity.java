package com.thilo20.machikoropad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Android entry point.
 * Initializes app-defined locale, then launches {@link MainActivity}.
 */
public class InitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_init);

        // try to restore language from saved settings
        SharedPreferences settings = getSharedPreferences("com.thilo20.machikoropad", MODE_PRIVATE);
        String lang = settings.getString("locale", "");
        if (!lang.isEmpty()) {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Configuration conf = getBaseContext().getResources().getConfiguration();
            conf.setLocale(locale);
            getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        }

        // launch main
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }
}
