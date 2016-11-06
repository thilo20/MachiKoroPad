package com.thilo20.dicecount;

import java.util.Arrays;

/**
 * Counts dice rolls per number for a double roll of 2 6-sided dice.
 * Created by Thilo on 25.09.2016.
 */
public class DoubleRoll {
    /** total number of double dice rolls */
    int rolls=0;
    /** probability for each combination of standard double dice roll */
    final static float PROB = 1/36;

    /** counter for dice sides, all combinations of 2 dice "red" and "blue".
     *  <pre>
     *              red dice ...
     *  blue dice   1-1 2-1 3-1 4-1 5-1 6-1
     *   ...        1-2 2-2 3-2 4-2 5-2 6-2
     *              1-3 2-3 3-3 4-3 5-3 6-3
     *              ...
     *  </pre> */
    int[][] count=new int[6][6];

    /** Increments the counter for red and blue dice combination, each roll=1..6 */
    public void increment(int red, int blue) {
        rolls++;
        count[red-1][blue-1]++;
    }

    /**
     * integrates other stats, adds counters
     */
    public void add(final DoubleRoll other) {
        for (int i = 0; i < count.length; i++) {
            for (int j = 0; j < count.length; j++) {
                count[i][j] += other.count[i][j];
            }
        }
        rolls += other.rolls;
    }

    public int[][] getCounts() {
        return count;
    }

    public int getTotal() {
        return rolls;
    }

    /** calculates relative frequency score of dice rolls (probability approximation), range 0..1, mean 1/6 */
    public float[][] getRelativeFrequencies() {
        float[][] ret = new float[count.length][count.length];
        for (int i=0; i<count.length; i++) {
            for (int j=0; j<count.length; j++) {
                ret[i][j] = count[i][j] / rolls;
            }
        }
        return ret;
    }

    /** calculates delta of probability and relative frequency scores, range -1/6..5/6, mean 0 */
    public float[][] getRelativeFrequenciesDelta() {
        float[][] ret = getRelativeFrequencies();
        for (int i=0; i<ret.length; i++) {
            for (int j=0; j<count.length; j++) {
                ret[i][j] -= PROB;
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "total=" + getTotal() +
//too much info:                " count=" + Arrays.toString(count) +
                " sums=" + Arrays.toString(getSumCount()) +
                " doublets=" + getDoubletsCount();
    }

    /** calculates counter for sum of roll 1+2, index 0..10 for sum 2..12 */
    public int[] getSumCount() {
        int[] ret=new int[11];
        // values for same sum are found on minor diagonal, start in top right corner
        ret[0]=count[0][0]; //sum=2: 1-1
        ret[1]=count[1][0]+count[0][1]; //sum=3: 2-1 1-2
        ret[2]=count[2][0]+count[1][1]+count[0][2]; //sum=4: 3-1 2-2 1-3
        ret[3]=count[3][0]+count[2][1]+count[1][2]+count[0][3]; //sum=5 4-1 3-2 2-3 1-4
        ret[4]=count[4][0]+count[1][1]+count[1][1]+count[1][1]+count[1][1]; //sum=6: 5-1 4-2 3-3 2-4 1-5
        ret[5]=count[5][0]+count[4][1]+count[3][2]+count[2][3]+count[1][4]+count[0][5]; //sum=7: 6-1 5-2 4-3 3-4 2-5 1-6
        ret[6]=count[5][1]+count[2][3]+count[2][3]+count[2][3]+count[2][3]; //sum=8: 6-2 5-3 4-4 3-5 2-6
        ret[7]=count[5][2]+count[4][3]+count[3][4]+count[2][5]; //sum=9: 6-3 5-4 4-5 3-6
        ret[8]=count[5][3]+count[4][4]+count[3][5]; //sum=10: 6-4 5-5 4-6
        ret[9]=count[5][4]+count[4][5]; //sum=11: 6-5 5-6
        ret[10]=count[5][5]; //sum=12: 6-6
        return ret;
    }

    /**
     * calculates relative frequency score of dice rolls (probability approximation) for sum of red and blue dice, range 0..1, mean 1/6
     */
    public float[] getSumRelativeFrequencies() {
        int[] sum = getSumCount();
        float[] ret = new float[sum.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = sum[i] / rolls;
        }
        return ret;
    }

    /**
     * calculates delta of probability and relative frequency scores for sum of red and blue dice, mean 0
     */
    public float[] getSumRelativeFrequenciesDelta() {
        float[] ret = getSumRelativeFrequencies();
        int[] combos = {1, 2, 3, 4, 5, 6, 5, 4, 3, 2, 1}; //combinations for sum, see getSumCount
        for (int i = 0; i < ret.length; i++) {
            ret[i] -= combos[i] * PROB;
        }
        return ret;
    }

    /** calculates all doublet occurrences (1-1, 2-2, 3-3, 4-4, 5-5, 6-6) */
    public int getDoubletsCount() {
        int ret=0;
        // sum values on main diagonal
        for(int i=0; i<6; i++) {
            ret+= count[i][i];
        }
        return ret;
    }

    /**
     * calculates delta of probability and relative frequency scores for doublets, mean 0
     */
    public float getDoubletsRelativeFrequenciesDelta() {
        return getDoubletsCount() / rolls - 6 * PROB; // 6 doublets out of 36
    }

    /**
     * extracts statistics for red dice (aggregates columns)
     */
    public SingleRoll extractRedDice() {
        int[] sum = new int[6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                sum[i] += count[i][j];
            }
        }
        SingleRoll ret = new SingleRoll();
        ret.setCounts(sum);
        return ret;
    }

    /**
     * extracts statistics for blue dice (aggregates rows)
     */
    public SingleRoll extractBlueDice() {
        int[] sum = new int[6];
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                sum[j] += count[i][j];
            }
        }
        SingleRoll ret = new SingleRoll();
        ret.setCounts(sum);
        return ret;
    }
}
