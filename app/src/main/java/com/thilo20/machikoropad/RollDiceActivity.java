package com.thilo20.machikoropad;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.thilo20.machikoro.Card;
import com.thilo20.machikoro.Game;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Screen to enter the dice roll result.
 * Supports rolling 1 or 2 dice (requires card train station).
 * Supports extra roll (requires card amusement park) when both dice show the same number.
 * Supports +2 (requires extension/card harbour) when dice sum is 10 or more.
 */
public class RollDiceActivity extends AppCompatActivity {

    Game game;

    // current dice roll
    int dice1 = 0;
    int dice2 = 0;
    boolean useDoubleRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_dice);
        // set back button on action bar
        //noinspection ConstantConditions
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get game
        game = Game.getInstance();

        // hide checkbox "Harbour" if playing without extension
        if (!game.isHarbour()) {
            CheckBox cbHarbour = (CheckBox) findViewById(R.id.cbHarbour);
            cbHarbour.setVisibility(View.INVISIBLE);
        }

        // get player state
        TextView tvPlayer = (TextView) findViewById(R.id.textView3);
        String caption = getResources().getString(R.string.roll_dice, game.getCurrentPlayer().getName());
        if (game.isExtraTurnNow()) {
            caption += " extra!";
        }
        tvPlayer.setText(caption);

        // update stats text
        TextView tvStats = (TextView) findViewById(R.id.textViewStats);
        tvStats.setText("Player " + game.getCurrentPlayer().getNumber() + " rolls: "
                + "\n" + game.getCurrentPlayer().getSingleRolls().toString()
                + "\n" + game.getCurrentPlayer().getDoubleRolls().toString());

        // wire dice buttons
        /** Called when the user clicked on one of the dice */
        initDiceButton(R.id.dice1, 1, R.drawable.one, R.drawable.one_inverted);
        initDiceButton(R.id.dice2, 2, R.drawable.two, R.drawable.two_inverted);
        initDiceButton(R.id.dice3, 3, R.drawable.three, R.drawable.three_inverted);
        initDiceButton(R.id.dice4, 4, R.drawable.four, R.drawable.four_inverted);
        initDiceButton(R.id.dice5, 5, R.drawable.five, R.drawable.five_inverted);
        initDiceButton(R.id.dice6, 6, R.drawable.six, R.drawable.six_inverted);

        // wire dice2 buttons
        /** Called when the user clicked on one of the dice */
        RadioButton rb;
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

        // has player used double roll last turn?
        useDoubleRoll = game.getCurrentPlayer().usesDoubleRoll();
        tb.setChecked(useDoubleRoll);
        // init dice2 matching toggle
        toggleDice2(null);

        // wire checkboxes
        CheckBox cbHarbour = (CheckBox) findViewById(R.id.cbHarbour);
        cbHarbour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add Harbour card
                game.getCurrentPlayer().getCards().add(Card.forName(Card.cardtype.HARBOUR));
            }
        });
        cbHarbour.setChecked(game.getCurrentPlayer().hasHarbour());

        CheckBox cbAmusementPark = (CheckBox) findViewById(R.id.cbExtraTurn);
        cbAmusementPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add AmusementPark card
                game.getCurrentPlayer().getCards().add(Card.forName(Card.cardtype.AMUSEMENT_PARK));
            }
        });
        cbAmusementPark.setChecked(game.getCurrentPlayer().hasAmusementPark());
    }

    private void initDiceButton(@IdRes int buttonId, final int number, @DrawableRes final int imageIdStart, @DrawableRes final int imageIdSelected) {
        RadioButton rb = (RadioButton) findViewById(buttonId);
        rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diceRolled(v, number);
            }
        });
        rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setButtonDrawable(imageIdSelected);
                } else {
                    buttonView.setButtonDrawable(imageIdStart);
                }
            }
        });
    }

    public void diceRolled(View view, int number) {
        dice1 = number;

        if (useDoubleRoll) {
            // test if dice2 has not been clicked yet
            if (dice2 < 1) {
                return;
            }
        }

        nextAction();
    }

    public void dice2Rolled(View view, int number) {
        dice2 = number;

        // test if dice1 has not been clicked yet
        if (dice1 < 1) {
            return;
        }

        nextAction();
    }

    private void checkHarbour() {
        // Harbour: test for sum>=10, ask for "+2"
        if (game.isHarbour()
                && game.getCurrentPlayer().hasHarbour()
                && (dice1 + dice2 >= 10)) {
            // show dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(RollDiceActivity.this);
            builder.setTitle(R.string.use_harbour);
            builder.setMessage((dice1 + dice2) + " + 2 = " + (dice1 + dice2 + 2));
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "harbour used, sum=" + (dice1 + dice2 + 2));
                    game.getCurrentPlayer().getRollResult().increment(dice1 + dice2 + 2);
                    checkExtraTurn();
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Logger.getLogger(getClass().getName()).log(Level.INFO, "harbour not used, sum=" + (dice1 + dice2));
                    game.getCurrentPlayer().getRollResult().increment(dice1 + dice2);
                    checkExtraTurn();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            game.getCurrentPlayer().getRollResult().increment(dice1 + dice2);
            checkExtraTurn();
        }
    }

    private void checkExtraTurn() {
        // AmusementPark: test for doublets, give extra turn
        if (!game.isExtraTurnNow()
                && game.getCurrentPlayer().hasAmusementPark()
                && dice1 == dice2) {
            // provide extra turn
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Extra turn granted");
            game.activateExtraTurn();

            // show info dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(RollDiceActivity.this);
            builder.setTitle(R.string.amusementpark);
            builder.setMessage(R.string.amusementpark_message);
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setCancelable(false);
            builder.setPositiveButton(R.string.nice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    nextTurn();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            // no extra turn provided
            nextTurn();
        }
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
        // remember choice
        game.getCurrentPlayer().setUsesDoubleRoll(useDoubleRoll);

        checkHarbour();
    }

    private void nextTurn() {
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

    @Override
    public void onBackPressed() {
        // redirect to main menu
        onNavigateUp();
    }
}
