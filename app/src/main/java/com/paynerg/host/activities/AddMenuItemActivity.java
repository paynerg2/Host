package com.paynerg.host.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.paynerg.host.R;
import com.paynerg.host.models.Menu;
import com.paynerg.host.models.MenuItem;
import com.paynerg.host.util.DatabaseHelper;

public class AddMenuItemActivity extends AppCompatActivity implements View.OnClickListener{

    private String menuName;
    private DatabaseHelper dbHelper;

    private EditText editName;
    private EditText editDescription;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu_item);

        if(savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                menuName = null;
            } else {
                menuName = extras.getString(getString(R.string.EXTRA_MENU_NAME));
            }
        } else {
            menuName = (String) savedInstanceState
                    .getSerializable(getString(R.string.EXTRA_MENU_NAME));
        }

        dbHelper = new DatabaseHelper(this);

        init();
    }

    private void init() {
        editName = findViewById(R.id.edit_menu_item_name);
        editDescription = findViewById(R.id.edit_menu_item_description);

        buttonSubmit = findViewById(R.id.button_submit_menu_item_form);
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_submit_menu_item_form:
                String menuItemName = editName.getText().toString();
                String description = editDescription.getText().toString();

                addMenuItem(menuItemName, description);

                editName.setText("");
                editDescription.setText("");

                Toast.makeText(AddMenuItemActivity.this, "Created successfully!",
                        Toast.LENGTH_SHORT);

                Intent intent = new Intent(AddMenuItemActivity.this,
                        MenuContentsActivity.class);
                intent.putExtra(getString(R.string.EXTRA_MENU_NAME), menuName);
                startActivity(intent);
                break;
        }
    }

    private void addMenuItem(String menuItemName, String description) {
        if(TextUtils.isEmpty(description)){
            editDescription.setError("Please enter a short description");
            editDescription.requestFocus();
        }
        if(TextUtils.isEmpty(menuItemName)){
            editName.setError("Enter Name");
            editName.requestFocus();
        }

        // TODO: This could be a bottleneck, find a better way to share
        //          the menu id.
        Menu menu = dbHelper.getMenuByName(menuName);

        MenuItem menuItem = new MenuItem(menuItemName, description);
        menuItem.setMenu_id(menu.getId());

        Log.i("addmenuitem", "menu id:");
        Log.i("addmenuitem", String.valueOf(menu.getId()));

        dbHelper.createMenuItem(menuItem);
    }
}