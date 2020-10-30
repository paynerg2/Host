package com.paynerg.host.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.paynerg.host.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    /* Allow a user to begin hosting or search for a hosted event.
     */
    private Button btnHostEvent;
    private Button btnJoinEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initInstances();
    }

    private void initInstances() {
        btnHostEvent = findViewById(R.id.button_host_event);
        btnJoinEvent = findViewById(R.id.buttton_join_event);

        btnHostEvent.setOnClickListener(this);
        btnJoinEvent.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_host_event:
                Intent intent = new Intent(MainActivity.this, HostActivity.class);
                startActivity(intent);
                break;
            case R.id.buttton_join_event:
                // Open join activity
                break;
            default:
                break;
        }
    }
}