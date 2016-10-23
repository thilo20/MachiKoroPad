package com.thilo20.machikoropad;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.*;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // set "app version" info text
        TextView tv = (TextView) findViewById(R.id.versionName);
        tv.setText(getResources().getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);
        
        // enable for testing
        if (false) {
            demoBarChart();
        } else {
            // hide UI component
            BarChart barChart = (BarChart) findViewById(R.id.chart);
            barChart.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Demonstrates showing a bar chart using lib MPAndroidChart.
     * source url=https://www.numetriclabz.com/android-bar-chart-using-mpandroidchart-library-tutorial/
     */
    private void demoBarChart() {
        // To make vertical bar chart, initialize graph id this way<br />
        BarChart barChart = (BarChart) findViewById(R.id.chart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(12f, 3));
        entries.add(new BarEntry(18f, 4));
        entries.add(new BarEntry(9f, 5));

        BarDataSet dataset = new BarDataSet(entries, "# of Calls");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        /* for create Grouped Bar chart
        ArrayList<BarEntry> group1 = new ArrayList<>();
        group1.add(new BarEntry(4f, 0));
        group1.add(new BarEntry(8f, 1));
        group1.add(new BarEntry(6f, 2));
        group1.add(new BarEntry(12f, 3));
        group1.add(new BarEntry(18f, 4));
        group1.add(new BarEntry(9f, 5));

        ArrayList<BarEntry> group2 = new ArrayList<>();
        group2.add(new BarEntry(6f, 0));
        group2.add(new BarEntry(7f, 1));
        group2.add(new BarEntry(8f, 2));
        group2.add(new BarEntry(12f, 3));
        group2.add(new BarEntry(15f, 4));
        group2.add(new BarEntry(10f, 5));

        BarDataSet barDataSet1 = new BarDataSet(group1, "Group 1");
        //barDataSet1.setColor(Color.rgb(0, 155, 0));
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);

        BarDataSet barDataSet2 = new BarDataSet(group2, "Group 2");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);

        ArrayList<BarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);
        */

        BarData data = new BarData(labels, dataset);
        barChart.setData(data);

        // COLOR PLAYGROUND //
        // predefined
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);

        // not working as expected
        dataset.setColor(R.color.colorSingleRoll); // unresolved gray!
        // recommended here: https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Colors
        // getResources().getColor(R.color.colorSingleRoll, getTheme()); // requires API level 23, not 15.. unuseable

        // from code
        int color = Color.rgb(255, 0, 0);
        dataset.setColor(color); // red

        // explicit color resolving with util
        List<Integer> colors = ColorTemplate.createColors(getResources(), new int[]{R.color.colorSingleRoll, R.color.colorDoubleRoll});
        dataset.setColors(new int[]{R.color.colorSingleRoll, color}); // 1: unresolved to gray, 2: red
        dataset.setColors(colors); // mixed, alternating color

        // set single resolved color
        dataset.setColor(colors.get(0)); // resolved R.color.colorSingleRoll
        // dataset.setColor(colors.get(1)); // resolved R.color.colorDoubleRoll

        barChart.animateY(5000);
    }

}
