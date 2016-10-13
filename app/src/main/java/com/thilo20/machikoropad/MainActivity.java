package com.thilo20.machikoropad;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.thilo20.machikoro.Game;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    /**
     * the current Machikoro game
     */
    Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // load current game
        // TODO: Game implements Serializable, better use android.util.JsonReader!
//        savedInstanceState.getSerializable();
        game = Game.getInstance();

        // disable "continue game" if no game is active
        Button btContinueGame = (Button) findViewById(R.id.btContinueGame);
        btContinueGame.setEnabled(game != null);

        // disable "statistics" if no game is active
        Button btShowStats = (Button) findViewById(R.id.btStats);
        btShowStats.setEnabled(game != null);

        initDiceRoller();
    }

    public void startNewGame(View view) {
        // alert if a game is already there
        if (game != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("A game is in progress. Do you want to dismiss it?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(MainActivity.this, NewGameActivity.class);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            Intent intent = new Intent(this, NewGameActivity.class);
            startActivity(intent);
        }
    }

    public void continueGame(View view) {
        if (game != null) {
            Intent intent = new Intent(this,
                    game.getStep() == 0 ? RollDiceActivity.class : BuyCardActivity.class);
            startActivity(intent);
        }
    }

    public void showStats(View view) {
        if (game != null) {
            Intent intent = new Intent(this, StatsActivity.class);
            startActivity(intent);
        }
    }

    public void openSettings(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void showAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /////////////////
    // dice roll source taken from "Android Dice Roller": url=http://tekeye.biz/2013/android-dice-code
    /////////////////
    ImageView dice_picture;        //reference to dice picture
    Random rng = new Random();    //generate random numbers
    SoundPool dice_sound = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
    int sound_id;        //Used to control sound stream return by SoundPool
    Handler handler;    //Post message to start roll
    Timer timer = new Timer();    //Used to implement feedback to user
    boolean rolling = false;        //Is die rolling?

    void initDiceRoller() {
        //load dice sound
        sound_id = dice_sound.load(this, R.raw.shake_dice, 1);
        //get reference to image widget
        dice_picture = (ImageView) findViewById(R.id.imageView1);
        //link handler to callback
        handler = new Handler(callback);
    }

    //User pressed dice, lets start
    public void HandleClick(View arg0) {
        if (!rolling) {
            rolling = true;
            //Show rolling image
            dice_picture.setImageResource(R.drawable.dice3droll);
            //Start rolling sound
            dice_sound.play(sound_id, 1.0f, 1.0f, 0, 0, 1.0f);
            //Pause to allow image to update
            timer.schedule(new Roll(), 400);
        }
    }

    //When pause completed message sent to callback
    class Roll extends TimerTask {
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    //Receives message from timer to start dice roll
    Callback callback = new Callback() {
        public boolean handleMessage(Message msg) {
            //Get roll result
            //Remember nextInt returns 0 to 5 for argument of 6
            //hence + 1
            switch (rng.nextInt(6) + 1) {
                case 1:
                    dice_picture.setImageResource(R.drawable.one);
                    break;
                case 2:
                    dice_picture.setImageResource(R.drawable.two);
                    break;
                case 3:
                    dice_picture.setImageResource(R.drawable.three);
                    break;
                case 4:
                    dice_picture.setImageResource(R.drawable.four);
                    break;
                case 5:
                    dice_picture.setImageResource(R.drawable.five);
                    break;
                case 6:
                    dice_picture.setImageResource(R.drawable.six);
                    break;
                default:
            }
            rolling = false;    //user can press again
            return true;
        }
    };

    //Clean up
    @Override
    protected void onPause() {
        super.onPause();
        dice_sound.pause(sound_id);
    }

    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

}
