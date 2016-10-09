package com.thilo20.machikoropad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.thilo20.dicecount.SingleRoll;
import com.thilo20.machikoro.Game;
import com.thilo20.machikoro.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RollDiceActivity extends AppCompatActivity {

    Game game;

    // current dice roll
    int dice1 = -1;
    int dice2 = -1;
    boolean useDoubleRoll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_dice);
        // set back button on action bar
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get game
        game = Game.getInstance();

        // get player state
        TextView tvPlayer = (TextView) findViewById(R.id.textView3);
        tvPlayer.setText(game.getCurrentPlayer().getName() + " würfelt");
        useDoubleRoll = game.getCurrentPlayer().usesDoubleRoll();

        // update stats text
        TextView tvStats=(TextView)findViewById(R.id.textViewStats);
        tvStats.setText("Player " + game.getCurrentPlayer().getNumber() + " rolls: "
                + "\n" + game.getCurrentPlayer().getSingleRolls().toString()
                + "\n" + game.getCurrentPlayer().getDoubleRolls().toString());

        // wire dice buttons
        /** Called when the user clicked on one of the dice */
        RadioButton rb = (RadioButton) findViewById(R.id.dice1);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceRolled(v, 1);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice2);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceRolled(v, 2);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice3);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceRolled(v, 3);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice4);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceRolled(v, 4);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice5);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceRolled(v, 5);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice6);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceRolled(v, 6);
            }
        });

        // wire dice2 buttons
        /** Called when the user clicked on one of the dice */
        rb = (RadioButton) findViewById(R.id.dice21);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice2Rolled(v, 1);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice22);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice2Rolled(v, 2);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice23);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice2Rolled(v, 3);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice24);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice2Rolled(v, 4);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice25);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice2Rolled(v, 5);
            }
        });
        rb = (RadioButton) findViewById(R.id.dice26);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dice2Rolled(v, 6);
            }
        });

        ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDice2(v);
            }
        });
        tb.setTextOn("use single roll");
        tb.setTextOff("use double roll");
        // has player used double roll last turn?
        tb.setChecked(useDoubleRoll);

        // init dice2 matching toggle
        toggleDice2(null);
    }

    public void diceRolled(View view, int number) {
        dice1 = number;

        if (useDoubleRoll) {
            // test if dice2 has already been clicked
            if (dice2 < 0) {
                return;
            }
        }

        nextAction();
    }

    public void dice2Rolled(View view, int number) {
        dice2 = number;

        // test if dice1 has already been clicked
        if (dice1 < 0) {
            return;
        }

        nextAction();
    }


    private void nextAction() {
        Logger log = Logger.getLogger(getClass().getName());
        if (useDoubleRoll) {
            log.log(Level.INFO, "Player " + game.getCurrentPlayer().getNumber() + " rolled " + dice1 + "+" + dice2 + "=" + (dice1 + dice2));
            // increment counter
            game.getCurrentPlayer().getDoubleRolls().increment(dice1, dice2);
        } else {
            log.log(Level.INFO, "Player " + game.getCurrentPlayer().getNumber() + " rolled " + dice1);
            // increment counter
            game.getCurrentPlayer().getSingleRolls().increment(dice1);
        }

        // proceed
        game.getCurrentPlayer().setUsesDoubleRoll(useDoubleRoll);
        game.nextTurn();

        Intent intent = new Intent(this, RollDiceActivity.class);
        startActivity(intent);

//later...
//        Intent intent = new Intent(this, BuyCardActivity.class);
//        startActivity(intent);
    }

    public void toggleDice2(View view) {
        ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton);
        if (tb.isChecked()) {
            // show dice2 buttons
            RadioButton rb = (RadioButton) findViewById(R.id.dice21);
            rb.setVisibility(View.VISIBLE);
            rb = (RadioButton) findViewById(R.id.dice22);
            rb.setVisibility(View.VISIBLE);
            rb = (RadioButton) findViewById(R.id.dice23);
            rb.setVisibility(View.VISIBLE);
            rb = (RadioButton) findViewById(R.id.dice24);
            rb.setVisibility(View.VISIBLE);
            rb = (RadioButton) findViewById(R.id.dice25);
            rb.setVisibility(View.VISIBLE);
            rb = (RadioButton) findViewById(R.id.dice26);
            rb.setVisibility(View.VISIBLE);
        } else {
            // hide dice2 buttons
            RadioButton rb = (RadioButton) findViewById(R.id.dice21);
            rb.setVisibility(View.INVISIBLE);
            rb = (RadioButton) findViewById(R.id.dice22);
            rb.setVisibility(View.INVISIBLE);
            rb = (RadioButton) findViewById(R.id.dice23);
            rb.setVisibility(View.INVISIBLE);
            rb = (RadioButton) findViewById(R.id.dice24);
            rb.setVisibility(View.INVISIBLE);
            rb = (RadioButton) findViewById(R.id.dice25);
            rb.setVisibility(View.INVISIBLE);
            rb = (RadioButton) findViewById(R.id.dice26);
            rb.setVisibility(View.INVISIBLE);
        }
        // update member
        useDoubleRoll = tb.isChecked();
    }
}
