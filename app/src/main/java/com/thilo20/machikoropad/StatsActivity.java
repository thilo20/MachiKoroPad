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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.thilo20.dicecount.DoubleRoll;
import com.thilo20.dicecount.RollResult;
import com.thilo20.dicecount.SingleRoll;
import com.thilo20.machikoro.Game;
import com.thilo20.machikoro.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Screens showing the statistics after or during the game.
 * Provides visualizations using bar charts.
 * Swipe left/right to see per-player details.
 */
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
    static BarChart barChart;


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
        // note: suppress options menu, has no function yet!
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_stats, menu);
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
         *
         * section content:
         * <pre>
         *     0: all players dice rolls single vs. double roll
         *     1: all players roll results by player, player colors
         *     2..(2+num players): each player's dice rolls single vs. double roll
         * </pre>
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

            // page index
            int pageIndex = getArguments().getInt(ARG_SECTION_NUMBER);

            // init stats for this page
            initStats(pageIndex);

            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            String playerName;
            if (pageIndex < 2) {
                playerName = getString(R.string.all_players);
            } else {
                playerName = game.getPlayer(pageIndex - 2).getName();
            }

            // fill text using format string template from resource
            textView.setText(getString(
                    R.string.section_format,
                    playerName,
                    createStatsText(pageIndex)
            ));

            // To make vertical bar chart, initialize graph id this way
            barChart = (BarChart) rootView.findViewById(R.id.chart);
            barChart.animateY(1000);
            barChart.getXAxis().setLabelsToSkip(0);
            barChart.setDescription(null);
            barChart.getLegend().setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
            barChart.getLegend().setTextSize(14);

            if (pageIndex == 0) {
                // fill chart with stacked bars showing player vs. player
                createBarChartStackedPlayers(barChart);
            } else {
                // fill chart with stacked bars showing single vs. double roll
                createBarChartStacked(barChart);
                // fill chart
//                createBarChart(barChart);
            }

            return rootView;
        }

        private void initStats(int sectionNumber) {
            if (game != null) {
                if (sectionNumber == 1) {
                    // create aggregate stats
                    sr = new SingleRoll();
                    dr = new DoubleRoll();
                    rr = new RollResult();
                    for (int i = 0; i < game.getNumPlayers(); i++) {
                        sr.add(game.getPlayer(i).getSingleRolls());
                        dr.add(game.getPlayer(i).getDoubleRolls());
                        rr.add(game.getPlayer(i).getRollResult());
                    }
                } else if (sectionNumber == 0) {
                    // do nothing
                } else {
                    // use player stats
                    sr = game.getPlayer(sectionNumber - 2).getSingleRolls();
                    dr = game.getPlayer(sectionNumber - 2).getDoubleRolls();
                    rr = game.getPlayer(sectionNumber - 2).getRollResult();
                }
            }
        }

        private String createStatsText(int sectionNumber) {
            if (game != null) {
                StringBuilder sb = new StringBuilder();

                if (sectionNumber == 0) {
                    sb.append(getResources().getString(R.string.stats_overall, game.getRounds(), game.getTurns()));
                }

                // text shows same data as chart, skip it!
                boolean showText = false;
                if (showText) {
                    sb.append("\nSingle rolls: 1..6\n");
                    sb.append(sr.toString());
                    sb.append("\nDouble rolls: 2..12\n");
                    sb.append(dr.toString());
                    sb.append("Roll results: 1..14\n");
                    sb.append(rr.toString());
                }
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

                // explicit color resolving with util, see AboutActivity#demoBarChart
                List<Integer> colors = ColorTemplate.createColors(getResources(), new int[]{R.color.colorSingleRoll, R.color.colorDoubleRoll});
                //dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                dataset.setColor(colors.get(0));
                dataset2.setColor(colors.get(1));

                BarData data = new BarData(labels, dataset);
                data.addDataSet(dataset2);
                barChart.setData(data);
            }
        }

        /**
         * Creates the stacked bar chart showing counters visually.
         */
        private void createBarChartStacked(BarChart barChart) {
            if (game != null) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                // create stacked bar chart

                // fill single rolls, 1..6
                // and  double roll sums, 2..12  into list of bar entries
                int[] counts1 = sr.getCounts();
                int[] counts2 = dr.getSumCount();
                // loop dice roll sums 1..12
                for (int i = 0; i < counts2.length + 1; i++) {
                    float[] vals = {0f, 0f};
                    if (i < counts1.length) {
                        vals[0] = (float) counts1[i];
                    }
                    if (i > 0) {
                        vals[1] = (float) counts2[i - 1];
                    }
                    entries.add(new BarEntry(vals, i));
                }

                BarDataSet dataset = new BarDataSet(entries, getString(R.string.stats_chart_legend));
                dataset.setStackLabels(new String[]{
                        getString(R.string.stats_chart_legend_single, sr.getTotal()),
                        getString(R.string.stats_chart_legend_double, dr.getTotal())
                });

                ArrayList<String> labels = new ArrayList<>();
                // fill label for dice roll sums, 1..12
                for (int i = 0; i < counts2.length + 1; i++) {
                    labels.add(Integer.toString(i + 1));
                }

                // explicit color resolving with util, see AboutActivity#demoBarChart
                List<Integer> colors = ColorTemplate.createColors(getResources(), new int[]{R.color.colorSingleRoll, R.color.colorDoubleRoll});
                //dataset.setColors(ColorTemplate.COLORFUL_COLORS);
                dataset.setColors(colors);

                BarData data = new BarData(labels, dataset);
                barChart.setData(data);

                dataset.setDrawValues(false);
                // try drawing data labels larger but with integer number - strange visual effects!
//                data.setValueTextSize(14);
//                data.setValueFormatter(new ValueFormatter() {
//                    @Override
//                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
//                        return String.format(Locale.US, "%1.0f", value);
//                    }
//                });
            }
        }

        /**
         * Creates the stacked bar chart showing results player vs. player.
         */
        private void createBarChartStackedPlayers(BarChart barChart) {
            if (game != null) {
                ArrayList<BarEntry> entries = new ArrayList<>();
                // create stacked bar chart

                // fill roll results per player
                // loop dice roll sums 1..14
                int len = game.isHarbour() ? 14 : 12;
                for (int i = 0; i < len; i++) {
                    float[] vals = new float[game.getNumPlayers()];
                    for (int pl = 0; pl < game.getNumPlayers(); pl++) {
                        vals[pl] = (float) game.getPlayer(pl).getRollResult().getCount()[i];
                    }
                    entries.add(new BarEntry(vals, i));
                }

                BarDataSet dataset = new BarDataSet(entries, "");

                String[] names = new String[game.getNumPlayers()];
                for (int pl = 0; pl < game.getNumPlayers(); pl++) {
                    names[pl] = game.getPlayer(pl).getName();
                }
                dataset.setStackLabels(names);

                ArrayList<String> labels = new ArrayList<>();
                // fill label for dice roll sums, 1..12
                for (int i = 0; i < len; i++) {
                    labels.add(Integer.toString(i + 1));
                }

                // explicit color resolving with util, see AboutActivity#demoBarChart
                int[] playercolors = new int[game.getNumPlayers()];
                for (int pl = 0; pl < game.getNumPlayers(); pl++) {
                    playercolors[pl] = game.getPlayer(pl).getColor();
                }
//                List<Integer> colors = ColorTemplate.createColors(getResources(), playercolors);
//                dataset.setColors(colors);
                dataset.setColors(playercolors);

                BarData data = new BarData(labels, dataset);
                barChart.setData(data);

                dataset.setDrawValues(false);
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
            // Show "2 + numPlayers" total pages.
            return 2 + game.getNumPlayers();
        }

        /**
         * unused
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position < 2) {
                return "Statistics for all players";
            } else if (position < 2 + game.getNumPlayers()) {
                return "Statistics for " + game.getPlayer(position - 2).getName();
            }
            return null;
        }
    }

}
