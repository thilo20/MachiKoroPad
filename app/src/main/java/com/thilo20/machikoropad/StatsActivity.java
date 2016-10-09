package com.thilo20.machikoropad;

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

import com.thilo20.dicecount.DoubleRoll;
import com.thilo20.dicecount.SingleRoll;
import com.thilo20.machikoro.Game;

import java.util.Arrays;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            textView.setText(getString(
                    R.string.section_format,
                    getArguments().getInt(ARG_SECTION_NUMBER),
                    createStatsText(getArguments().getInt(ARG_SECTION_NUMBER))
            ));

            return rootView;
        }

        private String createStatsText(int sectionNumber) {
            if (game != null) {
                StringBuffer sb = new StringBuffer();
                SingleRoll sr;

                if (sectionNumber == 0) {
                    sb.append("\nGame progress:");
                    sb.append("\nRound: " + game.getRounds());
                    sb.append("\nTurns: " + game.getTurns());

                    // create aggregate stats
                    sr = new SingleRoll();
                    for (int i = 0; i < game.getNumPlayers(); i++) {
                        sr.add(game.getPlayer(i).getSingleRolls());
                    }
                } else {
                    // use player stats
                    sr = game.getPlayer(sectionNumber - 1).getSingleRolls();
                }

                sb.append("\nSingle rolls: 1..6\n");
                sb.append(sr.toString());
                sb.append("\n");
                sb.append(Arrays.toString(sr.getRelativeFrequenciesDelta()));

                return sb.toString();
            }
            return "no game - no statistics.";
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
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
