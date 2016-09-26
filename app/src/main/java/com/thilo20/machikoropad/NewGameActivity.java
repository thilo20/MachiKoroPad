package com.thilo20.machikoropad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
        byte players=2;
        Button radio = (Button) findViewById(R.id.radioButton2);
        if(radio.isSelected()){
            players=3;
        } else {
            radio = (Button) findViewById(R.id.radioButton3);
            if(radio.isSelected())
                players=4;
        }

        // init game
        Game game = Game.getInstance();
        game.initGame(players);

        // player 1 rolls..
        Intent intent = new Intent(this, RollDiceActivity.class);
        startActivity(intent);
    }

}
