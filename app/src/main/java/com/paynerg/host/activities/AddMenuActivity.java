package com.paynerg.host.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.paynerg.host.R;
import com.paynerg.host.models.Menu;
import com.paynerg.host.util.DatabaseHelper;

public class AddMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editMenuName;
    private Button buttonSubmit;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        init();

        dbHelper = new DatabaseHelper(this);
    }

    private void init() {
        editMenuName = findViewById(R.id.edit_menu_name);
        buttonSubmit = findViewById(R.id.button_submit_menu_form);

        buttonSubmit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_submit_menu_form:
                // Create the new menu with given name
                String menuName = editMenuName.getText().toString();

                addMenu(menuName);

                // Clear edit text
                editMenuName.setText("");

                Toast.makeText(AddMenuActivity.this, "Created successfully!",
                        Toast.LENGTH_SHORT);

                // TODO: Should open the activity to add menu items, with the new menu selected.
                //       Probably need to pass the menu name to get that.
                Intent intent = new Intent(AddMenuActivity.this,
                        MenuContentsActivity.class);
                intent.putExtra(getString(R.string.EXTRA_MENU_NAME), menuName);
                startActivity(intent);
                break;
        }
    }

    private void addMenu(String menuName) {
        if(TextUtils.isEmpty(menuName)){
            editMenuName.setError("Enter Name");
            editMenuName.requestFocus();
            return;
        }

        Menu newMenu = new Menu(menuName);

        dbHelper.createMenu(newMenu);
    }
}