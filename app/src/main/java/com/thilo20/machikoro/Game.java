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

    // in game
    int currentPlayer = 0;
    int step = 0; // 0: roll dice, 1: buy card
    boolean hasExtraTurn = false;

    // stats
    int turns = 0;
    int rounds = 0;

    public static Game instance=null;

    public static Game getInstance() {
        if (instance==null) {
            instance=new Game();
        }
        return instance;
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

    public void nextTurn() {
        turns++;
        if (!hasExtraTurn) nextPlayer();
    }

    public void nextPlayer() {
        currentPlayer++;
        if (currentPlayer >= numPlayers) {
            currentPlayer = 0;
            rounds++;
        }
    }

    public void initGame(int numPlayers) {
        this.numPlayers = numPlayers;
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
