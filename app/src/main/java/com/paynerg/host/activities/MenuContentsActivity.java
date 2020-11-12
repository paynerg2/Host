package com.paynerg.host.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.paynerg.host.R;
import com.paynerg.host.adapters.MenuItemAdapter;
import com.paynerg.host.models.MenuItem;
import com.paynerg.host.util.DatabaseHelper;

import java.util.List;

public class MenuContentsActivity extends AppCompatActivity implements View.OnClickListener {

    private String menuName;

    private Button buttonAddMenuItem;
    private RecyclerView recyclerViewMenuItems;
    private RecyclerView.Adapter adapter;

    private List<MenuItem> menuItems;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_contents);

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

        // TODO: Prevent this from being a blocking call.
        //       Present a skeleton view while loading.
        dbHelper = new DatabaseHelper(this);
        menuItems = dbHelper.getAllMenuItemsByMenu(menuName);

        init();
    }


    private void init() {
        recyclerViewMenuItems = findViewById(R.id.recycler_view_menu);

        buttonAddMenuItem = findViewById(R.id.button_add_menu_item);
        buttonAddMenuItem.setOnClickListener(this);

        adapter = new MenuItemAdapter(this, menuItems);
        recyclerViewMenuItems.setHasFixedSize(false);
        recyclerViewMenuItems.setAdapter(adapter);
        recyclerViewMenuItems.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.button_add_menu_item:
                Intent intent = new Intent(MenuContentsActivity.this,
                        AddMenuItemActivity.class);
                intent.putExtra(getString(R.string.EXTRA_MENU_NAME), menuName);
                startActivity(intent);
                break;
        }
    }
}