package com.thilo20.dice;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Tests for DiceCount class
 */
public class DiceCountTest {
    @Test
    public void only_ones_rolled() throws Exception {
        DiceCount dc = new DiceCount();
        for(int i=0; i<10; i++) {
            dc.increment(1);
        }
        assertEquals(10, dc.getTotal());
        int[] ones={10, 0,0,0,0,0};
        assertArrayEquals(ones, dc.getCounts());
    }

    @Test
    public void everything_rolled() throws Exception {
        DiceCount dc = new DiceCount();
        for(int i=0; i<10; i++) {
            dc.increment(1+i%6);
        }
        assertEquals(10, dc.getTotal());
        int[] everything={2,2,2,2,1,1};
        assertArrayEquals(everything, dc.getCounts());
    }

    @Test
    public void random_rolled() throws Exception {
        Random rd=new Random();
        DiceCount dc = new DiceCount();
        for(int i=0; i<1000; i++) {
            dc.increment(1+rd.nextInt(6));
        }
        assertEquals(1000, dc.getTotal());
        float[] prob={1/6,1/6,1/6,1/6,1/6,1/6};
        assertArrayEquals(prob, dc.getRelativeFrequencies(), 0.01f);
    }
}