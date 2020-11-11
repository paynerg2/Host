package com.paynerg.host.interfaces;

public interface Connectable {
    public static final String TAG = null;
    public boolean isConnected = false;

    public boolean isConnected();
    void setConnected(boolean connected);
    public String getTAG();
}
