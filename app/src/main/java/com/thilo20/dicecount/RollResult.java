package com.thilo20.dicecount;

/**
 * Machikoropad visualizes statistics for the cardgame Machi Koro.
 * Copyright (C) 2016 Thilo Schaper <thilo20@e.mail.de>
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Arrays;

/**
 * Counts dice roll result occurrences.
 */
public class RollResult {

    /**
     * total number of rolls
     */
    int rolls = 0;

    /**
     * counter for dice roll results (1 dice 1-6 or sum of 2 dice 2-12, with possible +2 for harbour) range 1-14.
     */
    int[] count = new int[14];

    /**
     * Increments the counter for each roll=1..14
     */
    public void increment(int sum) {
        rolls++;
        count[sum - 1]++;
    }

    /**
     * integrates other stats, adds counters
     */
    public void add(final RollResult other) {
        for (int i = 0; i < count.length; i++) {
            count[i] += other.count[i];
        }
        rolls += other.rolls;
    }

    /**
     * returns counter for dice roll results with possible +2 for harbour, index 0..14 for numbers 1..14
     */
    public int[] getCount() {
        return count;
    }

    public int getTotal() {
        return rolls;
    }

    @Override
    public String toString() {
        return "total=" + rolls +
                ", count=" + Arrays.toString(count);
    }
}
