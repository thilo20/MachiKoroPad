package com.thilo20.machikoropad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.thilo20.dicecount.SingleRoll;
import com.thilo20.machikoro.Game;
import com.thilo20.machikoro.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RollDiceActivity extends AppCompatActivity {

    Game game;
    SingleRoll dc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_dice);

        // get game
        game = Game.getInstance();
        // get counter
        dc = game.getCurrentPlayer().getSingleRolls();

        // update text with stats
        TextView tvStats=(TextView)findViewById(R.id.textViewStats);
        tvStats.setText("Player " + game.getCurrentPlayer().getNumber() + " stats: " + dc.toString());
    }

    /** Called when the user clicked on one of the dice */
    public void diceRolled1(View view) {
        diceRolled(view, 1);
    }
    public void diceRolled2(View view) {
        diceRolled(view, 2);
    }
    public void diceRolled3(View view) {
        diceRolled(view, 3);
    }
    public void diceRolled4(View view) {
        diceRolled(view, 4);
    }
    public void diceRolled5(View view) {
        diceRolled(view, 5);
    }
    public void diceRolled6(View view) {
        diceRolled(view, 6);
    }

    public void diceRolled(View view, int number) {
        Logger.getLogger(getClass().getName()).log(Level.INFO, "Player " + game.getCurrentPlayer().getNumber() + " rolled " + number);

        // increment counter
        dc.increment(number);
        Logger.getLogger(getClass().getName()).log(Level.INFO, dc.toString());

        Intent intent = new Intent(this, BuyCardActivity.class);
        startActivity(intent);
    }

}
