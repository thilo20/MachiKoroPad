package com.thilo20.machikoropad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.thilo20.machikoro.Game;

public class NewGameActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.thilo20.machikoropad.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_game);
    }

    /** Called when the user clicks the Start button */
    public void startNewGame(View view) {
        // check players
        int players = 2;
        RadioGroup radio = (RadioGroup) findViewById(R.id.rgNumPlayers);
        switch (radio.getCheckedRadioButtonId()) {
            case R.id.radioButton1:
                players = 1;
                break;
            case R.id.radioButton2:
                players = 2;
                break;
            case R.id.radioButton3:
                players = 3;
                break;
            case R.id.radioButton4:
                players = 4;
                break;
            default:
        }

        // init game
        Game game = new Game();
        game.initGame(players);
        Game.setInstance(game);

        // player 1 rolls..
        Intent intent = new Intent(this, RollDiceActivity.class);
        startActivity(intent);
    }

}
