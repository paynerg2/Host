package com.paynerg.host.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.paynerg.host.R;
import com.paynerg.host.fragments.EnterPinDialogFragment;
import com.paynerg.host.util.Constants;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, EnterPinDialogFragment.EnterPinDialogListener {

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
                Intent intent = new Intent(MainActivity.this, MenuSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.buttton_join_event:
                // Open join activity
                FragmentManager fm = getSupportFragmentManager();
                EnterPinDialogFragment enterPinDialogFragment
                        = EnterPinDialogFragment.newInstance("Enter PIN");
                enterPinDialogFragment.show(fm, "fragment_enter_pin");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFinishEnterPinDialog(String inputText) {
        Toast.makeText(this, "received input text" + inputText, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this, GuestActivity.class);
        intent.putExtra(Constants.GuestActivityExtras.EXTRA_PIN, inputText);
        startActivity(intent);
    }
}