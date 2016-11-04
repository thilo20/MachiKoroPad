package com.thilo20.machikoro;

import android.graphics.Color;

import java.io.Serializable;

/**
 * A single game of Machikoro.
 * Created by Thilo on 05.09.2016.
 */
public class Game implements Serializable {
    // new game
    int numPlayers = 2;
    boolean harbour;

    // stats
    int turns = 0;
    int rounds = 0;

    // in game
    int currentPlayer = 0;
    int step = 0; // 0: roll dice, 1: buy card
    boolean extraTurnNext = false; // true if next turn is an extra turn
    boolean extraTurnNow = false; // true if current turn is an extra turn

    // current game instance
    public static Game instance=null;

    public static Game getInstance() {
        return instance;
    }

    public static void setInstance(Game game) {
        instance = game;
    }

    Player[] players;

    public int getNumPlayers() {
        return numPlayers;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public boolean isHarbour() {
        return harbour;
    }

    public void setHarbour(boolean harbour) {
        this.harbour = harbour;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayer];
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getPlayer(int index) {
        return players[index];
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public int getRounds() {
        return rounds;
    }

    public void setRounds(int rounds) {
        this.rounds = rounds;
    }

    /**
     * enables extra turn for the current player as next turn
     */
    public void activateExtraTurn() {
        extraTurnNext = true;
    }

    /**
     * true if the current turn is an extra turn
     */
    public boolean isExtraTurnNow() {
        return extraTurnNow;
    }

    public void nextTurn() {
        turns++;
        if (extraTurnNext) {
            // next turn is the extra turn
            extraTurnNow = true;
            extraTurnNext = false;
        } else {
            nextPlayer();
        }
    }

    public void nextPlayer() {
        extraTurnNow = false;
        extraTurnNext =false;

        currentPlayer++;
        if (currentPlayer >= numPlayers) {
            currentPlayer = 0;
            rounds++;
        }
    }

    public void initGame(int numPlayers, boolean harbour) {
        this.numPlayers = numPlayers;
        this.harbour = harbour;
        players = new Player[numPlayers];

        if (numPlayers > 0) {
            players[0] = new Player("Amy", 1, Color.RED);
        }
        if (numPlayers > 1) {
            players[1] = new Player("Bob", 2, Color.GREEN);
        }
        if (numPlayers > 2) {
            players[2] = new Player("Cee", 3, Color.BLUE);
        }
        if (numPlayers > 3) {
            players[3] = new Player("Dau", 4, Color.YELLOW);
        }
    }
}
