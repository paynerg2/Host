package com.paynerg.host.models;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuItem {
    private String itemName;
    private String description;

    public MenuItem(String itemName, String description) {
        this.itemName = itemName;
        this.description = description;
    }

    public MenuItem(String itemName) {
        this.itemName = itemName;
        this.description = "";
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
