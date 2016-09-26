package com.thilo20.machikoro;

import android.graphics.Color;

import com.thilo20.dicecount.DoubleRoll;
import com.thilo20.dicecount.SingleRoll;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A single Machikoro player.
 * Created by Thilo on 05.09.2016.
 */
public class Player {
    // player UI basics
    String name;
    byte number;
    Color color;

    // dice count stats
    SingleRoll singleRolls = new SingleRoll();
    DoubleRoll doubleRolls = new DoubleRoll();

    // game specific
    int coins = 0;
    List<Card> cards = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getNumber() {
        return number;
    }

    public void setNumber(byte number) {
        this.number = number;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public SingleRoll getSingleRolls() {
        return singleRolls;
    }

    public DoubleRoll getDoubleRolls() {
        return doubleRolls;
    }

    public int getCoins() {
        return coins;
    }

    public List<Card> getCards() {
        return cards;
    }
}
