package com.thilo20.dice;

import java.util.Arrays;

/**
 * Counts dice rolls per number for a single roll of 1 6-sided dice.
 * Created by Thilo on 06.09.2016.
 */
public class DiceCount {
    /** total number of dice rolls */
    int rolls=0;
    /** probability for each side of standard dice roll */
    final static float PROB = 1/6;

    /** counter for dice sides */
    int[] count={0,0,0,0,0,0};

    /** roll=1..6 */
    public void increment(int roll) {
        rolls++;
        count[roll-1]++;
    }

    /** integrates other stats, adds counters */
    public void add(final DiceCount other) {
        for (int i = 0; i < count.length; i++) {
            count[i] += other.count[i];
        }
        rolls += other.rolls;
    }

    public int[] getCounts() {
        return count;
    }

    public int getTotal() {
        return rolls;
    }

    /** calculates relative frequency score of dice rolls (probability approximation), range 0..1, mean 1/6 */
    public float[] getRelativeFrequencies() {
        float[] ret = new float[count.length];
        for (int i=0; i<count.length; i++) {
            ret[i]=count[i]/rolls;
        }
        return ret;
    }

    /** calculates delta of probability and relative frequency scores, range -1/6..5/6, mean 0 */
    public float[] getRelativeFrequenciesDelta() {
        float[] ret = getRelativeFrequencies();
        for (int i=0; i<ret.length; i++) {
            ret[i] -= PROB;
        }
        return ret;
    }

    @Override
    public String toString() {
        return "DiceCount{" +
                "total="+getTotal()+
                " count=" + Arrays.toString(count) +
                '}';
    }

    // test
    static DiceCount instance=null;
    public static DiceCount getInstance(){
        if(instance==null)
            instance=new DiceCount();
        return instance;
    }
}
