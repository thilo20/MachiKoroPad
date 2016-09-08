package com.thilo20.dice;

import java.util.Arrays;

/**
 * Counts dice rolls per number
 * Created by Thilo on 06.09.2016.
 */
public class DiceCount {
    int[] count={0,0,0,0,0,0};

    /** roll=1..6 */
    public void increment(int roll) {
        count[roll-1]++;
    }

    public int[] getCounts() {
        return count;
    }

    public int getTotal() {
        return count[0]+count[1]+count[2]+count[3]+count[4]+count[5];
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
