package com.thilo20.machikoro;

import java.io.Serializable;

/**
 * A single game of Machikoro.
 * Created by Thilo on 05.09.2016.
 */
public class Game implements Serializable {
    // new game
    byte players=2;
    boolean harbour;

    // in game
    byte currentPlayer=0;
    byte step=0; // 0: roll dice, 1: buy card

    // stats
    byte turns=0;
    byte rounds=0;

    public static Game instance=null;

    public static Game getInstance() {
        if (instance==null) {
            instance=new Game();
        }
        return instance;
    }

    public byte getPlayers() {
        return players;
    }

    public void setPlayers(byte players) {
        this.players = players;
    }

    public boolean isHarbour() {
        return harbour;
    }

    public void setHarbour(boolean harbour) {
        this.harbour = harbour;
    }

    public byte getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(byte currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public byte getStep() {
        return step;
    }

    public void setStep(byte step) {
        this.step = step;
    }

    public byte getTurns() {
        return turns;
    }

    public void setTurns(byte turns) {
        this.turns = turns;
    }

    public byte getRounds() {
        return rounds;
    }

    public void setRounds(byte rounds) {
        this.rounds = rounds;
    }


}
