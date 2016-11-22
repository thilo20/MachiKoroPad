package com.thilo20.machikoropad;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.thilo20.dicecount.DoubleRoll;
import com.thilo20.dicecount.RollResult;
import com.thilo20.dicecount.SingleRoll;
import com.thilo20.machikoro.Game;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    static Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        // get current game
        game = Game.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        // stats for this page
        SingleRoll sr;
        DoubleRoll dr;
        RollResult rr;

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_stats, container, false);

            // init stats for this page
            initStats(getArguments().getInt(ARG_SECTION_NUMBER));

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            String playerName;
            int playerIdx = getArguments().getInt(ARG_SECTION_NUMBER);
            if (playerIdx == 0) {
                playerName = getString(R.string.all_players);
            } else {
                playerName = game.getPlayer(playerIdx - 1).getName();
            }

            // fill text using format string template from resource
            textView.setText(getString(
                    R.string.section_format,
                    playerName,
                    createStatsText(getArguments().getInt(ARG_SECTION_NUMBER))
            ));

            // To make vertical bar chart, initialize graph id this way
            BarChart barChart = (BarChart) rootView.findViewById(R.id.chart);
            // fill chart
            createBarChart(barChart);

            return rootView;
        }

        private void initStats(int sectionNumber) {
            if (game != null) {
                if (sectionNumber == 0) {
                    // create aggregate stats
                    sr = new SingleRoll();
                    dr = new DoubleRoll();
                    rr = new RollResult();
                    for (int i = 0; i < game.getNumPlayers(); i++) {
                        sr.add(game.getPlayer(i).getSingleRolls());
                        dr.add(game.getPlayer(i).getDoubleRolls());
                        rr.add(game.getPlayer(i).getRollResult());
                    }
                } else {
                    // use player stats
                    sr = game.getPlayer(sectionNumber - 1).getSingleRolls();
                    dr = game.getPlayer(sectionNumber - 1).getDoubleRolls();
                    rr = game.getPlayer(sectionNumber - 1).getRollResult();
                }
            }
        }

        private String createStatsText(int sectionNumber) {
            if (game != null) {
                StringBuilder sb = new StringBuilder();

                if (sectionNumber == 0) {
                    sb.append(getResources().getString(R.string.stats_overall, game.getRounds(), game.getTurns()));
                }
                sb.append("Roll results: 1..14\n");
                sb.append(rr.toString());
                sb.append("\nSingle rolls: 1..6\n");
                sb.append(sr.toString());
                sb.append("\nDouble rolls: 2..12\n");
                sb.append(dr.toString());

                return sb.toString();
            }
            // never happens because button/activity is disabled when no game exists
            return "no game - no statistics.";
        }

        /**
         * Creates the bar chart showing counters visually.
         */
        private void createBarChart(BarChart barChart) {
            if (game != null) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                ArrayList<BarEntry> entries2 = new ArrayList<>();
                // create simple bar chart

                // fill single rolls, 1..6
                int[] counts = sr.getCounts();
                for (int i = 0; i < counts.length; i++) {
                    entries.add(new BarEntry(counts[i], i));
                }
                // fill double roll sums, 2..12
                counts = dr.getSumCount();
                for (int i = 0; i < counts.length; i++) {
                    entries2.add(new BarEntry(counts[i], i + 1));
                }

                BarDataSet dataset = new BarDataSet(entries, "# of single rolls: " + sr.getTotal());
                BarDataSet dataset2 = new BarDataSet(entries2, "# of double rolls: " + dr.getTotal());

                ArrayList<String> labels = new ArrayList<>();
                // fill label for dice roll sums, 1..12
                for (int i = 0; i < 12; i++) {
                    labels.add(Integer.toString(i + 1));
                }

                BarData data = new BarData(labels, dataset);
                data.addDataSet(dataset2);

                // explicit color resolving with util, see AboutActivity#demoBarChart
                List<Integer> colors = ColorTemplate.createColors(getResources(), new int[]{R.color.colorSingleRoll, R.color.colorDoubleRoll});
                //dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                dataset.setColor(colors.get(0));
                dataset2.setColor(colors.get(1));

                barChart.setData(data);
                barChart.animateY(1000);
            }
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show "1 + numPlayers" total pages.
            return 1 + game.getNumPlayers();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Statistics for all players";
            } else if (position < game.getNumPlayers() + 1) {
                return "Statistics for " + game.getPlayer(position - 1).getName();
            }
            return null;
        }
    }

}
