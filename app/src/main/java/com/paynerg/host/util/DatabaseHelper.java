package com.paynerg.host.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.paynerg.host.models.Menu;
import com.paynerg.host.models.MenuItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    public static final String TAG = "DatabaseHelper";

    private static String DATABASE_NAME = "menuManager";
    public static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_MENU_ITEM = "menu_items";
    public static final String TABLE_MENU = "menu";

    // Menu Table Columns
    public static final String KEY_MENU_ID = "id";
    public static final String KEY_MENU_NAME = "name";
    public static final String KEY_CREATION_DATE = "creation_date";

    // Menu Item Table Columns
    public static final String KEY_ITEM_ID = "id";
    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String FOREIGN_KEY_MENU_ID = "menu_id";

    // Table Create Statements
    public static final String CREATE_TABLE_MENU_ITEMS = "CREATE TABLE "
            + TABLE_MENU_ITEM + "(" + KEY_ITEM_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ITEM_NAME + " TEXT NOT NULL, "
            + KEY_DESCRIPTION + " TEXT NOT NULL, "
            + FOREIGN_KEY_MENU_ID + " INTEGER REFERENCES " + TABLE_MENU
            + "); ";

    public static final String CREATE_TABLE_MENU = "CREATE TABLE "
            + TABLE_MENU + "(" + KEY_MENU_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MENU_NAME + " TEXT NOT NULL, "
            + KEY_CREATION_DATE + " DATETIME );";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        Log.d(TAG, CREATE_TABLE_MENU);
        Log.d(TAG, CREATE_TABLE_MENU_ITEMS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MENU);
        db.execSQL(CREATE_TABLE_MENU_ITEMS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  TABLE_MENU);
        db.execSQL("DROP TABLE IF EXISTS " +  TABLE_MENU_ITEM);

        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * ===================== table methods: menu ===============================
     */

    public long createMenu(Menu menu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MENU_NAME, menu.getName());
        values.put(KEY_CREATION_DATE, getDateTime());

        return db.insert(TABLE_MENU, null, values);
    }

    public Menu getMenu(long menu_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MENU + " WHERE "
                + KEY_MENU_ID + " = " + menu_id;

        Log.i(TAG, selectQuery);

        Cursor c = db. rawQuery(selectQuery, null);

        if(c != null) {
            c.moveToFirst();
        }

        return getMenuFromRow(c);
    }

    public Menu getMenuByName(String menuName){
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MENU + " WHERE "
                + KEY_MENU_NAME + " = '" + menuName + "'";

        Log.i(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if(c != null) {
            c.moveToFirst();
        }

        return getMenuFromRow(c);
    }

    public List<Menu> getAllMenus() {
        List<Menu> menus = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MENU;

        Log.i(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                Menu menu = getMenuFromRow(c);
                menus.add(menu);
            } while (c.moveToNext());
        }

        return menus;
    }

    public int updateMenu(Menu menu) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MENU_NAME, menu.getName());
        values.put(KEY_CREATION_DATE, menu.getCreationDate());

        return db.update(TABLE_MENU, values, KEY_MENU_ID + " = ?",
                new String[] { String.valueOf(menu.getId()) });
    }

    public void deleteMenu(long menu_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU, KEY_MENU_ID + " = ?",
                new String[] { String.valueOf(menu_id) });
    }

    /**
     * ===================== table methods: menu item ===========================
     */

    public long createMenuItem(MenuItem menuItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, menuItem.getItemName());
        values.put(KEY_DESCRIPTION, menuItem.getDescription());
        values.put(FOREIGN_KEY_MENU_ID, menuItem.getMenu_id());

        return db.insert(TABLE_MENU_ITEM, null, values);
    }

    public List<MenuItem> getAllMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_MENU_ITEM;

        Log.i(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if(c.moveToFirst()) {
            do {
                MenuItem menuItem = getMenuItemFromRow(c);
                menuItems.add(menuItem);
            } while (c.moveToNext());
        }

        return menuItems;
    }

    public MenuItem getMenuItem(String menu_item_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_MENU_ITEM + " WHERE "
                + KEY_ITEM_ID + " = " + menu_item_id;

        Log.i(TAG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null) {
            c.moveToFirst();
        }

        return getMenuItemFromRow(c);
    }

    public List<MenuItem> getAllMenuItemsByMenu(String menu_name) {
        List<MenuItem> menuItems = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_MENU_ITEM + " mi, "
                + TABLE_MENU + " m WHERE m." + KEY_MENU_NAME + " = '"
                + menu_name + "' AND mi." + FOREIGN_KEY_MENU_ID + " = m."
                + KEY_MENU_ID;

        Log.i(TAG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()){
            do {
                MenuItem menuItem = getMenuItemFromRow(c);
                menuItems.add(menuItem);
            } while (c.moveToNext());
        }

        return menuItems;
    }

    public int updateMenuItem(MenuItem menuItem) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_NAME, menuItem.getItemName());
        values.put(KEY_DESCRIPTION, menuItem.getDescription());
        values.put(FOREIGN_KEY_MENU_ID, menuItem.getMenu_id());

        return db.update(TABLE_MENU_ITEM, values, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(menuItem.getId()) });
    }

    public void deleteMenuItem(long menu_item_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU_ITEM, KEY_ITEM_ID + " = ?",
                new String[] { String.valueOf(menu_item_id) });
    }


    private Menu getMenuFromRow(Cursor c) {
        String menuName = c.getString(c.getColumnIndex(KEY_MENU_NAME));
        String creationDate = c.getString(c.getColumnIndex(KEY_CREATION_DATE));
        Menu menu = new Menu(menuName, creationDate);
        menu.setId(c.getInt(c.getColumnIndex(KEY_MENU_ID)));

        return menu;
    }

    private MenuItem getMenuItemFromRow(Cursor c) {
        String itemName = c.getString(c.getColumnIndex(KEY_ITEM_NAME));
        String description = c.getString(c.getColumnIndex(KEY_DESCRIPTION));
        MenuItem menuItem = new MenuItem(itemName, description);
        menuItem.setId(c.getInt(c.getColumnIndex(KEY_ITEM_ID)));
        menuItem.setMenu_id(c.getInt(c.getColumnIndex(FOREIGN_KEY_MENU_ID)));

        return menuItem;
    }

    /**
     * ========================================================================
     */

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if ( db != null && db.isOpen()) {
            db.close();
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
