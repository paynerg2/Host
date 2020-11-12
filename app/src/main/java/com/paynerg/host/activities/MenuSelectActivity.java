package com.paynerg.host.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.paynerg.host.R;

public class MenuSelectActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonCreateNewMenu;
    private Button buttonSelectMenu;
    private Button buttonBeginHosting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        initInstances();
    }

    private void initInstances() {
        buttonCreateNewMenu = findViewById(R.id.button_create_new_menu);
        buttonSelectMenu = findViewById(R.id.button_select_menu);
        buttonBeginHosting = findViewById(R.id.button_begin_hosting_event);

        buttonCreateNewMenu.setOnClickListener(this);
        buttonSelectMenu.setOnClickListener(this);
        buttonBeginHosting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.i("onclick", "clicked");
        switch(v.getId()){
            case R.id.button_create_new_menu:
                Log.i("onclick", "button_create_new_menu");
                Intent addMenuIntent = new Intent(MenuSelectActivity.this,
                        AddMenuActivity.class);
                startActivity(addMenuIntent);
                break;
            case R.id.button_select_menu:
                // Open fragment to choose one of the created menus
                break;
            case R.id.button_begin_hosting_event:
                Intent hostIntent = new Intent(MenuSelectActivity.this,
                        HostActivity.class);
                startActivity(hostIntent);
                break;
            default:
                break;
        }
    }
}