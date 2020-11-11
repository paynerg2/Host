package com.paynerg.host.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private String itemName;
    private String description;
    private int id;
    private int menu_id;

    public MenuItem(String itemName, String description) {
        this.itemName = itemName;
        this.description = description;
    }

    public MenuItem(String itemName) {
        this.itemName = itemName;
        this.description = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public JSONObject toJSON(){
        JSONObject obj = new JSONObject();
        try{
            obj.put("itemName", itemName);
            obj.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return obj;
    }
}
