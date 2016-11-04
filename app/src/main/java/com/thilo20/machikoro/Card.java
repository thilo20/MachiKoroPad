package com.thilo20.machikoro;

import java.util.HashMap;
import java.util.Map;

/**
 * A single card of the Machi Koro game.
 */
public class Card {
    public enum cardtype {
        HARBOUR, AMUSEMENT_PARK // more..
    };

    public enum cardcolor {
        BLUE, GREEN, RED, PURPLE, GOLD
    }

    int cost;
    cardtype name;
    cardcolor color;

    public Card(cardtype name, cardcolor color, int cost) {
        this.name = name;
        this.color = color;
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public cardtype getName() {
        return name;
    }

    public cardcolor getColor() {
        return color;
    }

    // deck of cards
    static Map<cardtype, Card> cardset = new HashMap<>();

    static {
        cardset.put(cardtype.HARBOUR, new Card(cardtype.HARBOUR, cardcolor.GOLD, 2));
        cardset.put(cardtype.AMUSEMENT_PARK, new Card(cardtype.AMUSEMENT_PARK, cardcolor.GOLD, 16));
    }

    public static Card forName(cardtype name) {
        return cardset.get(name);
    }
}
