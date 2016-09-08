package com.thilo20.machikoropad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuyCardActivity extends AppCompatActivity {

    private String[] cards = {"Weizenfeld", "Bäckerei", "Café", "Mini-Markt", "Wald", "Stadion", "Bahnhof"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_card);

        // current player, credits

        // populate list view
        ListView lv = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                cards);
        lv.setAdapter(arrayAdapter);
    }

    public void buyCard(View v) {
       //TODO: get selected list item
        ListView lv = (ListView) findViewById(R.id.listView);
        if(lv.getSelectedItemPosition() >=0) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Bought " + cards[lv.getSelectedItemPosition()]);
        } else {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "Bought nothing");
        }
        Intent intent = new Intent(this, RollDiceActivity.class);
        startActivity(intent);
    }
}
