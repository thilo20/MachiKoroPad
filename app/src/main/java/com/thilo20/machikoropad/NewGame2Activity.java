package com.thilo20.machikoropad;

import android.content.Intent;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thilo20.machikoro.Game;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PERSON_NAME;

/**
 * Step 2 for initializing a new game: asking for players names.
 */
public class NewGame2Activity extends AppCompatActivity {

    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // dynamic UI creation, uses no xml resource
        // setContentView(R.layout.activity_new_game2);

        game = Game.getInstance();

        // creating LinearLayout
        LinearLayout linLayout = new LinearLayout(this);
        // specifying vertical orientation
        linLayout.setOrientation(LinearLayout.VERTICAL);
        linLayout.setPadding(64, 16, 64, 16);
        // creating LayoutParams
        ActionBar.LayoutParams linLayoutParam = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        // set LinearLayout as a root element of the screen
        setContentView(linLayout, linLayoutParam);

        ActionBar.LayoutParams lpView = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tv = new TextView(this);
        tv.setText(R.string.enter_player_names);
        tv.setLayoutParams(lpView);
        linLayout.addView(tv);

        // create UI elements for numPlayers
        for (int i = 0; i < game.getNumPlayers(); i++) {
            EditText ed = new EditText(this);
            ed.setId(i);
            ed.setText(game.getPlayer(i).getName());
            ed.setLayoutParams(lpView);
            ed.setEms(20); // chars allowed for player name
            ed.setSelectAllOnFocus(true);
            ed.setInputType(TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PERSON_NAME);
            linLayout.addView(ed);
        }

        Button btn = new Button(this);
        btn.setText(R.string.start);
        linLayout.addView(btn, new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame(v);
            }
        });
    }

    /**
     * Called when the user clicks the Start button
     */
    public void startNewGame(View view) {
        // update player names
        for (int i = 0; i < game.getNumPlayers(); i++) {
            EditText ed = (EditText) findViewById(i);
            // name non-empty?
            if (!ed.getText().toString().isEmpty()) {
                // set new name
                game.getPlayer(i).setName(ed.getText().toString());
            }
        }

        // player 1 rolls..
        Intent intent = new Intent(this, RollDiceActivity.class);
        startActivity(intent);
    }
}
