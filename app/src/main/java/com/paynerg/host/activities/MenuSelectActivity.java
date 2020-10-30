package com.paynerg.host.activities;

import android.os.Bundle;
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
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_create_new_menu:
                // Open activity to create new menu
                break;
            case R.id.button_select_menu:
                // Open fragment to choose one of the created menus
                break;
            case R.id.button_begin_hosting_event:
                // Open the activity to find nearby devices and share the selected menu
                break;
            default:
                break;
        }
    }
}