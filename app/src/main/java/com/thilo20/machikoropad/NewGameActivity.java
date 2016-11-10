package com.thilo20.machikoropad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.thilo20.machikoro.Game;

/**
 * Initializes a new game asking for number of players and rules.
 * {@link NewGame2Activity} follows.
 */
public class NewGameActivity extends AppCompatActivity {

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

        // check rules
        CheckBox cb = (CheckBox) findViewById(R.id.cbHarbour);
        boolean harbour = cb.isChecked();

        // init game
        Game game = new Game();
        game.initGame(players, harbour);
        Game.setInstance(game);

        // set player names
        Intent intent = new Intent(this, NewGame2Activity.class);
        startActivity(intent);
    }

}
